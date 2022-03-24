package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.UserRepository

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }
    private val conversationRepository by lazy { ConversationRepository(db) }
    private val userRepo by lazy { UserRepository(db) }




    fun getMessages(convid:Long): LiveData<PagingData<ChatMessage>> {

        return conversationRepository.getMessages(convid)

    }
}