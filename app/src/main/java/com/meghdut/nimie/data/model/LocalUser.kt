package com.meghdut.nimie.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class LocalUser(
    @PrimaryKey
    val userId: Long,
    val publicKey: String,
    val privateKey: String,
    val name: String,
    val isActive: Int
)