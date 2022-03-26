package com.meghdut.nimie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meghdut.nimie.data.model.CacheEntry

@Dao
interface SQLCacheDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEntry(cacheEntry: CacheEntry)

    @Query("select data from cache_entry where hashKey=:key limit 1")
    fun getEntry(key: Int): String

    fun put(key: Int,value:String){
        insertEntry(CacheEntry(key,value))
    }
}
