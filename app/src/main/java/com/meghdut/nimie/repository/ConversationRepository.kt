package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient
import kotlinx.coroutines.Dispatchers

class ConversationRepository(db: NimieDb) {
    private val conversationDao = db.conversationDao()
    private val statusDao = db.statusDao()

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

}