package com.meghdut.nimie.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update
import com.meghdut.nimie.model.LocalUser

@Dao
interface LocalUserDao {

    @Insert(onConflict = IGNORE)
    fun insert(localUser: LocalUser)



    @Update
    fun update(localUser: LocalUser)

    @Query("DELETE FROM user_table")
    fun clearData()


    @Query("SELECT * from user_table where isActive = 1 ")
    fun getUser(): LocalUser

}