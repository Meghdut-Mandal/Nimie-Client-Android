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
    fun getEntry(key: Int): ByteArray

    @Query("select count(*)> 0 from cache_entry where hashKey=:key ")
    fun contains(key: Int): Boolean

    fun put(key: ByteArray, value: ByteArray) {
        val contentHashCode = key.contentHashCode()
        insertEntry(CacheEntry(contentHashCode, value))
    }

    fun getEntry(key:ByteArray): ByteArray {
        val contentHashCode = key.contentHashCode()
        return getEntry(contentHashCode)
    }
}
