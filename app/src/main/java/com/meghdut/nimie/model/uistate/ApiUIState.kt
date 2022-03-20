package com.meghdut.nimie.model.uistate

sealed class ApiUIState<T> {
    class Uninitialised<T> : ApiUIState<T>()

    class Loading<T>(val status: String) : ApiUIState<T>()

    class Done<T>(val result: T) : ApiUIState<T>()

    class Error<T>(val error: String, val throwable: Throwable? = null) : ApiUIState<T>()
}