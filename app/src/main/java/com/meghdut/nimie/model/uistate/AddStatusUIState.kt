package com.meghdut.nimie.model.uistate

import com.meghdut.nimie.model.LocalStatus

sealed class AddStatusUIState {
    object Uninitialised : AddStatusUIState()

    class Creating(val status: String) : AddStatusUIState()

    class Done(val localStatus: LocalStatus) : AddStatusUIState()

    class Error(val error: String) : AddStatusUIState()
}