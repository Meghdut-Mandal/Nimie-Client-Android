package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }
    private val conversationRepository by lazy { ConversationRepository(db) }
    private val userRepo by lazy { UserRepository(db) }

     val currentConversation = MutableLiveData<LocalConversation>()




    fun getMessages(convid:Long): LiveData<PagingData<ChatMessage>> {
         ioTask {
             val localConversation = conversationRepository.getConversation(convid)
             currentConversation.postValue(localConversation)
         }
        return conversationRepository.getMessages(convid)
    }
}