package com.meghdut.nimie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalUser
import com.meghdut.nimie.model.uistate.AddStatusUIState
import com.meghdut.nimie.model.uistate.SplashUIState
import com.meghdut.nimie.repository.StatusRepository
import com.meghdut.nimie.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val conversationDao by lazy { db.conversationDao() }

    private val userRepo by lazy { UserRepository(db) }

    private val statusRepository by lazy { StatusRepository(db) }
    private val userLiveData = MutableLiveData<LocalUser>()

    val addStatusLiveData = MutableLiveData<AddStatusUIState>()


    fun getConversationLiveData() = conversationDao.getConversationLive()

    fun getActiveUser(): LiveData<LocalUser> {
        dbTask {
            val currentActiveUser = userRepo.getCurrentActiveUser()
            userLiveData.postValue(currentActiveUser)
        }
        return userLiveData
    }

    fun addStatus(status:String) {
         dbTask {
             try {
                 addStatusLiveData.postValue(AddStatusUIState.Creating(status))
                 val currentActiveUser = userRepo.getCurrentActiveUser()
                 val createStatus = statusRepository.createStatus(status, currentActiveUser.userId)
                 addStatusLiveData.postValue(AddStatusUIState.Done(createStatus))
             }catch (e:Exception) {
                 addStatusLiveData.postValue(AddStatusUIState.Error(e.localizedMessage!!))
             }
         }
    }


    private fun dbTask(func:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            func()
        }
    }


}