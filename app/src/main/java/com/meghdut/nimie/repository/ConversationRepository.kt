package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.image.ImageCache
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.ContentType
import com.meghdut.nimie.data.model.ConversationKeyEntry
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.network.MessagingClient
import com.meghdut.nimie.ui.util.CryptoUtils
import com.meghdut.nimie.ui.util.time
import kotlinx.coroutines.Dispatchers

class ConversationRepository(val db: NimieDb, val imageCache: ImageCache) {
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
            val decryptedMessage = it.decryptMessage()
            addMsgToDb(decryptedMessage)
        }
        val lastMessageId = chatDao.getLatestMessageId(conversationId) ?: 0
        client.syncMessages(lastMessageId)

        chatClients[conversationId] = client
    }

    private fun addMsgToDb(decryptedMessage: ChatMessage) {
        if (decryptedMessage.contentType == ContentType.IMG) {
            // if it's a image message
            val cacheImageName = imageCache.cacheImage(decryptedMessage.message)
            val imageMessage = decryptedMessage.copy(message = cacheImageName.toByteArray())
            chatDao.insert(imageMessage)
        } else {
            // if its a text message
            chatDao.insert(decryptedMessage)
        }
    }

    /*
     This function only handles messages being sent out by the current client.
     */
    fun sendMessage(userId: Long, chatMessage: ChatMessage) {

        val unencryptedContent = chatMessage.message
        val encryptedMessage = time {
            return@time chatMessage.encryptMessage()
        }

        val recievedMsg = time {
            return@time GrpcClient.sendMessage(userId, encryptedMessage)
        }
        println("Sending done ")

        val unencryptedMessage = recievedMsg.copy(message = unencryptedContent)
       addMsgToDb(unencryptedMessage)
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

