package de.juliando.app.android.ui.utils

sealed class DataState {
    object Loading: DataState()
    class Success<T>(val value: T): DataState()
    class Error(val exception: Exception) : DataState()
}