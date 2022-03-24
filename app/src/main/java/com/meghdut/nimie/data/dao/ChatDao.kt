package com.meghdut.nimie.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.meghdut.nimie.data.model.ChatMessage

@Dao
interface ChatDao {

    @Insert(onConflict = REPLACE)
    fun insert(chatMessage: ChatMessage): Long

    @Query("SELECT * from chat_messages  where conversationId = :conversationId order by createTime")
    fun getMessagesPagingData(conversationId: Long): DataSource.Factory<Int, ChatMessage>

}