package com.meghdut.nimie.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.ui.model.SplashUIState
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask
import kotlinx.coroutines.runBlocking

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }
    private val userRepository by lazy { UserRepository(db) }

    val uiState = MutableLiveData<SplashUIState>(SplashUIState.Uninitialised)

    fun createLocalUser() = ioTask {
        uiState.postValue(SplashUIState.Working("Finding a sweet name for you!"))
        try {
            val user = userRepository.newUser()
            uiState.postValue(SplashUIState.Success(user))
        } catch (e: Exception) {
            e.printStackTrace()
            uiState.postValue(SplashUIState.Error(e.localizedMessage ?: ""))
        }
    }

    fun checkActiveUser() = ioTask {
        if (userRepository.anyActiveUser()){
            val user = userRepository.getCurrentActiveUser()
            uiState.postValue(SplashUIState.Success(user))
        }
    }
}