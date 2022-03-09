package com.meghdut.nimie.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import com.meghdut.nimie.model.ChatMessage

@Dao
interface ChatDao {

    @Insert(onConflict = ABORT)
    fun insert(chatMessage: ChatMessage): Long

    @Query("SELECT * from chat_messages  where conversationId = :conversationId AND createTime < :lastChatTime LIMIT 25")
    fun getMessages(conversationId: Long, lastChatTime: Long): List<ChatMessage>

    @Query("SELECT * from chat_messages  where conversationId = :conversationId order by createTime DESC  LIMIT 25")
    fun getMessages(conversationId: Long): List<ChatMessage>

    @Query("SELECT * from chat_messages  where conversationId = :conversationId order by createTime DESC  LIMIT 5")
    fun getMessagesLiveData(conversationId: Long): LiveData<List<ChatMessage>>

}