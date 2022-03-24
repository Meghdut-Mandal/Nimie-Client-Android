package com.meghdut.nimie.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


object ContentType {
    const val TXT = "text"
    const val IMG = "image"
}


@Entity(tableName = "chat_messages")
data class ChatMessage(
    @SerializedName("conversation_id")
    val conversationId: Long,
    @SerializedName("create_time")
    val createTime: Long,
    @SerializedName("message")
    val message: String,

    @SerializedName("isSeen")
    val isSeen: Boolean,

    // content type can be IMG, TXT
    @SerializedName("contentType")
    val contentType: String,

    @PrimaryKey
    @SerializedName("message_id")
    val messageId: Long,

    // 0 means outgoing, 1 means incoming
    @SerializedName("user_id")
    val userId: Long
)