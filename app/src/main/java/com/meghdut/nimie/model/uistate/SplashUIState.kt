package com.meghdut.nimie.model.uistate

import com.meghdut.nimie.model.LocalUser

sealed class SplashUIState {
    object Uninitialised : SplashUIState()
    class Working(val progress: String) : SplashUIState()
    class Error(val message: String) : SplashUIState()
    class Success(val localUser: LocalUser) : SplashUIState()
}