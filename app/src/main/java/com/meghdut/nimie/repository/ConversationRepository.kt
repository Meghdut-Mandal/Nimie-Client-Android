package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.network.MessagingClient
import kotlinx.coroutines.Dispatchers

class ConversationRepository(db: NimieDb) {
    private val conversationDao = db.conversationDao()
    private val statusDao = db.statusDao()
    private val chatDao = db.chatDao()
    private val chatClients = mutableMapOf<Long, MessagingClient>()

    fun replyConversation(
        reply: String,
        userId: Long,
        statusId: Long
    ): LocalConversation {

        val status = statusDao.getStatusById(statusId)
        val chat = GrpcClient.replyToStatus(reply, userId, statusId, status.userName)

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

    suspend fun getConversation(conversationId: Long): LocalConversation {
        return conversationDao.getConversation(conversationId)
    }

    fun openConversation(userId: Long, conversationId: Long) {
        val client = GrpcClient.startChatConversation(userId, conversationId) {
            chatDao.insert(it)
        }
        chatClients[conversationId] = client
    }

    fun sendMessage(chatMessage: ChatMessage){
        chatClients[chatMessage.conversationId]?.sendMessage(chatMessage)
    }
}