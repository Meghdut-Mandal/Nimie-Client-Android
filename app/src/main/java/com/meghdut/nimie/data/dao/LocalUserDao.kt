package com.meghdut.nimie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.meghdut.nimie.data.model.LocalUser

@Dao
interface LocalUserDao {

    @Insert(onConflict = REPLACE)
    fun insert(localUser: LocalUser)


    @Query("UPDATE user_table SET isActive=0 WHERE isActive=1")
    fun clearActiveStatus()

    @Query("SELECT count(*) from user_table WHERE isActive=1")
    fun anyActive(): Int

    @Update
    fun update(localUser: LocalUser)


    @Query("SELECT * from user_table where isActive = 1 ")
    fun getActiveUser(): LocalUser

}