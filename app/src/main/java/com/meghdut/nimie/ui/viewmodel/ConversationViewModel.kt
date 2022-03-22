package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.repository.ConversationRepository

class ConversationViewModel (application: Application) : AndroidViewModel(application) {


    fun loadConversations() {


    }

    private val db by lazy { NimieDb.create(application) }
    private val conversationRepository by lazy { ConversationRepository(db) }

    val conversation get() = conversationRepository.getConversations()


}