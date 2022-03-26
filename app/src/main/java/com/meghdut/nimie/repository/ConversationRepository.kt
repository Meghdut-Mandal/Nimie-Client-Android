package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.dao.SQLCacheDao
import com.meghdut.nimie.data.model.CacheEntry
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.network.MessagingClient
import com.meghdut.nimie.ui.util.CryptoUtils
import kotlinx.coroutines.Dispatchers

class ConversationRepository(db: NimieDb) {
    private val conversationDao = db.conversationDao()
    private val statusDao = db.statusDao()
    private val chatDao = db.chatDao()
    private val userDao = db.userDao()
    private val messageCache = db.getCacheEntry()

    private val chatClients = mutableMapOf<Long, MessagingClient>()
    private val conversationCache = mutableMapOf<Long, LocalConversation>()

    fun replyConversation(
        reply: String,
        userId: Long,
        statusId: Long
    ): LocalConversation {

        val status = statusDao.getStatusById(statusId)

        val encryptedReply = CryptoUtils.encrypt(reply, status.publicKey)

        messageCache.put(encryptedReply.hashCode(),reply)

        val chat = GrpcClient.replyToStatus(encryptedReply, userId, statusId, status.userName)

        conversationDao.insert(chat)

        return chat
    }

    fun getConversations(): LiveData<PagingData<LocalConversation>> {
        val dataSourceFactory = conversationDao.getConversationDataSource()
        return Pager(
            PagingConfig(100),
            null,
            dataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData
    }

    suspend fun loadConversations(userId: Long) {
        val conversationList = GrpcClient.getConversationList(userId)
        conversationDao.insertAll(conversationList)
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
        chatClients[chatMessage.conversationId]?.sendMessage(chatMessage.encryptMessage())
    }

    fun closeConversation(conversationId: Long) {
        chatClients[conversationId]?.closeChat()
    }

    private fun ChatMessage.decryptMessage(): ChatMessage {
        val currentActiveUser = userDao.getActiveUser()

        if (userId == 0L) {
        // if the message was sent by us look for it in the cache
            return copy(message = messageCache.getEntry(message.hashCode()))
        }

        val decryptedMessage = CryptoUtils.decrypt(message, currentActiveUser.privateKey)

        return copy(message = decryptedMessage)
    }

    private fun ChatMessage.encryptMessage(): ChatMessage {
        val conversation = getConversation(conversationId)
        val encryptedMessage = CryptoUtils.encrypt(message, conversation.otherPublicKey)

        // add the message to cache
        messageCache.put(encryptedMessage.hashCode(),message)

        return copy(message = encryptedMessage)
    }

}

