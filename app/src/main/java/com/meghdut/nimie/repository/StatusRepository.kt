package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.data.model.LocalStatus
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.ui.util.CryptoUtils
import kotlinx.coroutines.Dispatchers

class StatusRepository(db: NimieDb) {
    private val statusDao = db.statusDao()
    private val userDao = db.userDao()
    private val messageCache = db.getSqlCacheDao()
    private val conversationDao = db.conversationDao()


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

    fun createStatus(status: String, userId: Long): LocalStatus {
        val activeUser = userDao.getActiveUser()
        val createdStatus = GrpcClient.createStatus(status, userId,activeUser.publicKey,activeUser.name)
        statusDao.insert(createdStatus)

        return createdStatus
    }

    fun getStatus(): LiveData<PagingData<LocalStatus>> {
        val dataSourceFactory = statusDao.getStatus()
        return Pager(
            PagingConfig(100),
            null,
            dataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData
    }

    suspend fun loadStatus() {
        val bulkStatus = GrpcClient.getBulkStatus(0, 100)
        bulkStatus.forEach {
            println(it)
        }
        statusDao.insertMultipleStatus(bulkStatus)
    }

}