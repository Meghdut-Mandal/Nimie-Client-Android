package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.ContentType
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }
    private val conversationRepository by lazy { ConversationRepository(db) }
    private val userRepository by lazy { UserRepository(db) }
    private val currentActiveUser by lazy { userRepository.getCurrentActiveUser() }

     val currentConversation = MutableLiveData<LocalConversation>()



    fun sendMessage(text:String)= ioTask {
        val localConversation = currentConversation.value ?: return@ioTask
        val chatMessage = ChatMessage(localConversation.conversationId,0,text,false,ContentType.TXT,0,0)
         conversationRepository.sendMessage(chatMessage)
    }

    fun openConversation(convid: Long) = ioTask{
        conversationRepository.openConversation(currentActiveUser.userId,convid)
        println("Opned a conversation stream for $convid")
    }

    fun getMessages(convid:Long): LiveData<PagingData<ChatMessage>> {
         ioTask {
             val localConversation = conversationRepository.getConversation(convid)
             currentConversation.postValue(localConversation)
         }
        return conversationRepository.getMessages(convid)
    }
}