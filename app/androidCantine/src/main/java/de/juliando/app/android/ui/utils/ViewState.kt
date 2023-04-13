package de.juliando.app.android.ui.utils

sealed class ViewState {
    object Loading: ViewState()
    object Success: ViewState()
    class Error(exception: Exception): ViewState()
}
