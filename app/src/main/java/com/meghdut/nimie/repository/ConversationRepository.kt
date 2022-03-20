package com.meghdut.nimie.repository

import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.network.GrpcClient

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
}