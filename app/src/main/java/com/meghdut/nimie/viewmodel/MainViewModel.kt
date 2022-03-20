package com.meghdut.nimie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalConversation
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.model.LocalUser
import com.meghdut.nimie.model.uistate.ApiUIState
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.StatusRepository
import com.meghdut.nimie.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val conversationDao by lazy { db.conversationDao() }

    private val userRepo by lazy { UserRepository(db) }

    private val statusRepository by lazy { StatusRepository(db) }

    private val conversationRepository by lazy { ConversationRepository(db) }
    private val userLiveData = MutableLiveData<LocalUser>()

    val addStatusLiveData = MutableLiveData<ApiUIState<LocalStatus>>()

    val replyConLiveData = MutableLiveData<ApiUIState<LocalConversation>>()


    fun getConversationLiveData() = conversationDao.getConversationLive()

    fun getActiveUser(): LiveData<LocalUser> {
        ioTask {
            val currentActiveUser = userRepo.getCurrentActiveUser()
            userLiveData.postValue(currentActiveUser)
        }
        return userLiveData
    }

    fun addStatus(status: String) {
        ioTask {
            try {
                addStatusLiveData.postValue(ApiUIState.Loading(status))
                val currentActiveUser = userRepo.getCurrentActiveUser()
                val createStatus = statusRepository.createStatus(status, currentActiveUser.userId)
                addStatusLiveData.postValue(ApiUIState.Done(createStatus))
            } catch (e: Exception) {
                addStatusLiveData.postValue(ApiUIState.Error(e.localizedMessage!!))
            }
        }
    }

    fun replyStatus(reply: String, statusId: Long) {
        ioTask {
            try {
                replyConLiveData.postValue(ApiUIState.Loading(reply))
                val currentActiveUser = userRepo.getCurrentActiveUser()
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
    }


    private fun ioTask(func: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            func()
        }
    }

    fun loadStatus() {
        ioTask {
            statusRepository.loadStatus()
        }
    }

    fun getStatues() = statusRepository.getStatus()


}