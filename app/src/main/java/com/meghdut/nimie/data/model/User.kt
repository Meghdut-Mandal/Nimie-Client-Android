package com.meghdut.nimie.data.model

import androidx.room.Entity

@Entity(tableName = "user_keys")
data class User(val id: Long, val publicKey: String)
