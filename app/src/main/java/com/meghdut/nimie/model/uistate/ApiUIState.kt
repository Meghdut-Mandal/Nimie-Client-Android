package com.meghdut.nimie.model.uistate

import java.util.*

sealed class ApiUIState<T> {
    class Uninitialised<T> : ApiUIState<T>()

    class Creating<T>(val status: String) : ApiUIState<T>()

    class Done<T>(val localStatus: T) : ApiUIState<T>()

    class Error<T>(val error: String) : ApiUIState<T>()
}