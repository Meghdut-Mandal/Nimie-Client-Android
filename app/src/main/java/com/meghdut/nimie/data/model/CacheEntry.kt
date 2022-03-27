package com.meghdut.nimie.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_entry")
data class CacheEntry(
    @PrimaryKey
    val hashKey: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheEntry

        if (hashKey != other.hashKey) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hashKey
        result = 31 * result + data.contentHashCode()
        return result
    }
}