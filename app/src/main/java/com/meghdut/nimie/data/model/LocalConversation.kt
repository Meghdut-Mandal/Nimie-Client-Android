package com.meghdut.nimie.data.model

import androidx.room.ColumnInfo
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
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val lastText: ByteArray,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val otherPublicKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalConversation

        if (conversationId != other.conversationId) return false
        if (statusId != other.statusId) return false
        if (createTime != other.createTime) return false
        if (otherName != other.otherName) return false
        if (lastUpdateTime != other.lastUpdateTime) return false
        if (!lastText.contentEquals(other.lastText)) return false
        if (!otherPublicKey.contentEquals(other.otherPublicKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = conversationId.hashCode()
        result = 31 * result + statusId.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + otherName.hashCode()
        result = 31 * result + lastUpdateTime.hashCode()
        result = 31 * result + lastText.contentHashCode()
        result = 31 * result + otherPublicKey.contentHashCode()
        return result
    }
}