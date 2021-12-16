package com.meghdut.nimie.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @SerializedName("conversation_id")
    val conversationId: Long,
    @SerializedName("create_time")
    val createTime: Long,
    @SerializedName("message")
    val message: String,

    @PrimaryKey
    @SerializedName("message_id")
    val messageId: Long,
    @SerializedName("user_id")
    val userId: Long
)