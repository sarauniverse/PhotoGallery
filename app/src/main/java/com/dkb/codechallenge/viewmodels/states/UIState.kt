package com.dkb.codechallenge.viewmodels.states

sealed class UIState<T> {
    class STARTED<T>: UIState<T>()
    data class SUCCESS<T>(val obj: T): UIState<T>()
    data class ERROR<T>(val errorMessage: String, val exception: Exception?): UIState<T>()
}
