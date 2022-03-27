package com.meghdut.nimie.data.model


import androidx.room.ColumnInfo
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
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val message: ByteArray,

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
) {

    val textMessage get() = String(message)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatMessage

        if (conversationId != other.conversationId) return false
        if (createTime != other.createTime) return false
        if (!message.contentEquals(other.message)) return false
        if (isSeen != other.isSeen) return false
        if (contentType != other.contentType) return false
        if (messageId != other.messageId) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = conversationId.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + message.contentHashCode()
        result = 31 * result + isSeen.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + messageId.hashCode()
        result = 31 * result + userId.hashCode()
        return result
    }
}