package com.meghdut.nimie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_entry")
data class CacheEntry(
    @PrimaryKey
    val hashKey: Int,
    val data: String
)