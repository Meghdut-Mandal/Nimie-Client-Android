package com.meghdut.nimie.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.meghdut.nimie.data.model.LocalConversation

@Dao
interface ConversationDao {

    @Insert
    fun insert(localConversation: LocalConversation)

    @Update
    fun update(localConversation: LocalConversation)

    @Query("SELECT * from local_conversation where conversationId = :conversationId order by lastUpdateTime limit 1")
    fun getConversationById(conversationId: Long): LocalConversation

    @Query("SELECT * from local_conversation order by lastUpdateTime")
    fun getConversationLive(): LiveData<List<LocalConversation>>
}