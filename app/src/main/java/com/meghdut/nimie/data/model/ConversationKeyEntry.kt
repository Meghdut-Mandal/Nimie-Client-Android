package com.meghdut.nimie.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "conversation_keys")
data class ConversationKeyEntry(
    @PrimaryKey
    val conversationId:Long,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val aesKey:ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConversationKeyEntry

        if (conversationId != other.conversationId) return false
        if (!aesKey.contentEquals(other.aesKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = conversationId.hashCode()
        result = 31 * result + aesKey.contentHashCode()
        return result
    }
}