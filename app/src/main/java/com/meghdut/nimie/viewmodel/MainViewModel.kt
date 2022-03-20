package com.meghdut.nimie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalConversation
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.model.LocalUser
import com.meghdut.nimie.model.uistate.ApiUIState
import com.meghdut.nimie.repository.ConversationRepository
import com.meghdut.nimie.repository.StatusRepository
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val conversationDao by lazy { db.conversationDao() }

    private val userRepo by lazy { UserRepository(db) }

    private val userLiveData = MutableLiveData<LocalUser>()


    fun getConversationLiveData() = conversationDao.getConversationLive()

    fun getActiveUser(): LiveData<LocalUser> {
        ioTask {
            val currentActiveUser = userRepo.getCurrentActiveUser()
            userLiveData.postValue(currentActiveUser)
        }
        return userLiveData
    }


}