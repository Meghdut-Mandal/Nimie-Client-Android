package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.data.model.LocalStatus
import com.meghdut.nimie.ui.model.ApiUIState
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.StatusRepository
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val statusRepository by lazy { StatusRepository(db) }

    private val conversationRepository by lazy { ConversationRepository(db) }

    val addStatusLiveData = MutableLiveData<ApiUIState<LocalStatus>>()

    val replyConLiveData = MutableLiveData<ApiUIState<LocalConversation>>()

    private val userRepository by lazy { UserRepository(db) }


    fun addStatus(status: String) = ioTask {
        try {
            addStatusLiveData.postValue(ApiUIState.Loading(status))
            val currentActiveUser = userRepository.getCurrentActiveUser()
            val createStatus = statusRepository.createStatus(status, currentActiveUser.userId)
            addStatusLiveData.postValue(ApiUIState.Done(createStatus))
        } catch (e: Exception) {
            addStatusLiveData.postValue(ApiUIState.Error(e.localizedMessage!!))
        }

    }

    fun replyStatus(reply: String, statusId: Long) = ioTask {
        try {
            replyConLiveData.postValue(ApiUIState.Loading(reply))
            val currentActiveUser = userRepository.getCurrentActiveUser()
            val replyConversation = conversationRepository.replyConversation(
                reply,
                currentActiveUser.userId,
                statusId
            )
            replyConLiveData.postValue(ApiUIState.Done(replyConversation))
        } catch (e: Exception) {
            replyConLiveData.postValue(ApiUIState.Error(e.localizedMessage!!))
        }

    }


    fun loadStatus() = ioTask {
        statusRepository.loadStatus()
    }

    fun getStatues() = statusRepository.getStatus()

}