package de.juliando.app.android.utils

import androidx.annotation.StringRes

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

/**
 * Snackbar object to transfer the data, which is needed to show a snackbar.
 * @param message The name of the Snackbar message
 * @param button The button for a snackbar which contains a name and a action function. Can be null
 *
 **/
data class SnackbarItem(
    @StringRes val message: Int,
    val button: ButtonItem?
) {
    data class ButtonItem(
        @StringRes val name: Int,
        val action: suspend () -> Unit
    )

}
