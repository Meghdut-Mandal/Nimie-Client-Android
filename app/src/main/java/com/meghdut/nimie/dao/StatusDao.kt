package com.meghdut.nimie.dao

import androidx.room.Dao
import androidx.room.Insert
import com.meghdut.nimie.model.LocalStatus

@Dao
interface StatusDao {

    @Insert
    fun insert(localStatus: LocalStatus)
}