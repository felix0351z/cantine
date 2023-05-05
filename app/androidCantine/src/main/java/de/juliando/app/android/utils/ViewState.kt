package de.juliando.app.android.utils

sealed class ViewState {
    object Loading: ViewState()
    object Success: ViewState()
    class Error(exception: Exception): ViewState()
}

sealed class DataState {
    object Loading: DataState()

    data class Success<T: Any>(val value: T): DataState()

    class Error(exception: Exception): DataState()
}
