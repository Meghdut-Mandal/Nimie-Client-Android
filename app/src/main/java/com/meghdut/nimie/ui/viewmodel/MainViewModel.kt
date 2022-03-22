package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalUser
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val conversationDao by lazy { db.conversationDao() }

    private val userRepo by lazy { UserRepository(db) }

    private val userLiveData = MutableLiveData<LocalUser?>()


    fun getConversationLiveData() = conversationDao.getConversationDataSource()

    fun getActiveUser(): LiveData<LocalUser?> {
        ioTask {
            val currentActiveUser = userRepo.getCurrentActiveUser()
            userLiveData.postValue(currentActiveUser)
        }
        return userLiveData
    }

    fun logOutUser() = ioTask {
        userRepo.logOutUser()
        userLiveData.postValue(null)
    }


}