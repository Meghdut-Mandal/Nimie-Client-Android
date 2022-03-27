package com.meghdut.nimie.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class LocalUser(
    @PrimaryKey
    val userId: Long,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val publicKey: ByteArray,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val privateKey: ByteArray,
    val name: String,
    val isActive: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalUser

        if (userId != other.userId) return false
        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!privateKey.contentEquals(other.privateKey)) return false
        if (name != other.name) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        result = 31 * result + privateKey.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + isActive
        return result
    }
}