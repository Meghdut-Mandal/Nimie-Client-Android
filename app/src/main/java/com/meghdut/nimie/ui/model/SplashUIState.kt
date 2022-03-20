package com.meghdut.nimie.ui.model

import com.meghdut.nimie.data.model.LocalUser

sealed class SplashUIState {
    object Uninitialised : SplashUIState()
    class Working(val progress: String) : SplashUIState()
    class Error(val message: String) : SplashUIState()
    class Success(val localUser: LocalUser) : SplashUIState()
}