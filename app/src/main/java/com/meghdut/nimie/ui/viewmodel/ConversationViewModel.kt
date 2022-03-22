package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class ConversationViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }
    private val conversationRepository by lazy { ConversationRepository(db) }
    private val userRepo by lazy { UserRepository(db) }


    val conversation get() = conversationRepository.getConversations()


    fun loadConversations() = ioTask {
        val currentActiveUser = userRepo.getCurrentActiveUser()
        conversationRepository.loadConversations(currentActiveUser.userId)
    }
}