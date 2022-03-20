package com.meghdut.nimie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.uistate.SplashUIState
import com.meghdut.nimie.repository.UserRepository
import com.meghdut.nimie.ui.util.ioTask

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
}