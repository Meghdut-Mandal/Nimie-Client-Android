package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.ConversationKeyEntry
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.network.MessagingClient
import com.meghdut.nimie.ui.util.CryptoUtils
import com.meghdut.nimie.ui.util.time
import kotlinx.coroutines.Dispatchers

class ConversationRepository(db: NimieDb) {
    private val conversationDao = db.conversationDao()
    private val chatDao = db.chatDao()
    private val userDao = db.userDao()
    private val chatClients = mutableMapOf<Long, MessagingClient>()
    private val conversationCache = mutableMapOf<Long, LocalConversation>()


    fun getConversations(): LiveData<PagingData<LocalConversation>> {
        val dataSourceFactory = conversationDao.getConversationDataSource()
        return Pager(
            PagingConfig(100),
            null,
            dataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData
    }

    suspend fun loadConversations(userId: Long) {
        val conversationList = GrpcClient.getConversationList(userId).map {

            if (!conversationDao.hasConversationKey(it.conversationId)) {
                getConversationKey(it.conversationId)
            }

            val aesKey = conversationDao.getAESKey(it.conversationId)

            val decryptedText = CryptoUtils.decryptAES(it.lastText, aesKey)
            return@map it.copy(lastText = decryptedText)
        }

        conversationDao.insertAll(conversationList)
    }

    private fun getConversationKey(conversationId: Long) {
        val encodedAesKey = GrpcClient.exchangeKeyRequest(conversationId)
        val myPrivateKey = userDao.getActiveUser().privateKey
        val decryptedAesKey = CryptoUtils.decryptRSA(encodedAesKey, myPrivateKey)

        conversationDao.insert(ConversationKeyEntry(conversationId, decryptedAesKey))
    }

    fun getMessages(conversationId: Long): LiveData<PagingData<ChatMessage>> {
        val dataSourceFactory = chatDao.getMessagesPagingData(conversationId)
        return Pager(
            PagingConfig(100),
            null,
            dataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData
    }

    fun getConversation(conversationId: Long): LocalConversation {
        if (!conversationCache.containsKey(conversationId))
            conversationCache[conversationId] = conversationDao.getConversation(conversationId)

        return conversationCache[conversationId]!!
    }


    fun openConversation(userId: Long, conversationId: Long) {
        val client = GrpcClient.startChatConversation(userId, conversationId) {
            /*
             In this place both type of messages i.e.
                  * messages sent by the client encrypted with the pubic key of the other user
                  * messages sent by the other use encrypted with the public key of the current user.
             The 2nd type of messages are the one's which are actually decrypted the 1st type is actually called from cache.
             */
            chatDao.insert(it.decryptMessage())
        }
        val lastMessageId = chatDao.getLatestMessageId(conversationId) ?: 0
        client.syncMessages(lastMessageId)

        chatClients[conversationId] = client
    }

    /*
     This function only handles messages being sent out by the current client.
     */
    fun sendMessage(chatMessage: ChatMessage) {
        val msg= time {
          return@time chatMessage.encryptMessage()
        }

        println("Encrypted!  ")
        time {
            chatClients[chatMessage.conversationId]?.sendMessage(msg)
        }

        println("Sending done ")
    }

    fun closeConversation(conversationId: Long) {
        chatClients[conversationId]?.closeChat()
    }

    private fun ChatMessage.decryptMessage(): ChatMessage {
        val aesKey = conversationDao.getAESKey(conversationId)

        val decryptedMessage = CryptoUtils.decryptAES(message, aesKey)

        return copy(message = decryptedMessage)
    }

    private fun ChatMessage.encryptMessage(): ChatMessage {
        val aesKey = conversationDao.getAESKey(conversationId)
        val encryptedMessage = CryptoUtils.encryptAES(message, aesKey)

        return copy(message = encryptedMessage)
    }

}

