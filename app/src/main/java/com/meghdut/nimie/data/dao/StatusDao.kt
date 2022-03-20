package com.meghdut.nimie.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meghdut.nimie.data.model.LocalStatus

@Dao
interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localStatus: LocalStatus)

    @Query("SELECT * FROM local_status")
    fun getStatus(): LiveData<List<LocalStatus>>

    @Query("SELECT COUNT(statusId) from local_status")
    suspend fun count(): Int


    @Query("SELECT * from local_status where statusId = :id ")
    fun getStatusById(id: Long) : LocalStatus

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleStatus(list: List<LocalStatus>)
}