package com.meghdut.nimie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_conversation")
data class LocalConversation(
    @PrimaryKey
    val conversationId: Long,
    val statusId: Long,
    val createTime: Long,
    val otherName: String,
    val lastUpdateTime: Long,
    val lastText: String,
    val otherPublicKey: String
)