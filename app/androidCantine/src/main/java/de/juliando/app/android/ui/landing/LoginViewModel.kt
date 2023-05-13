package de.juliando.app.android.ui.landing

import androidx.lifecycle.ViewModel
import de.juliando.app.models.errors.HttpStatusException
import de.juliando.app.repository.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {

    private var _serverURLInput: MutableStateFlow<String> = MutableStateFlow("")
    val serverURLInput = _serverURLInput.asStateFlow()

    private var _usernameInput: MutableStateFlow<String> = MutableStateFlow("")
    val usernameInput = _usernameInput.asStateFlow()

    private var _passwordInput: MutableStateFlow<String> = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()

    private var _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    suspend fun signIn(): Boolean{
        return try {
            authenticationRepository.login(usernameInput.value, passwordInput.value)
            true
        }catch (e: HttpStatusException.UnauthorizedException){
            _errorMessage.value = "Benutzername und Passwort stimmen nicht überein"
            _passwordInput.value = ""
            false
        }
    }


    fun updateServerURLInput(text: String) {
        _serverURLInput.value = text
    }

    fun updateUsernameInput(text: String) {
        _usernameInput.value = text
    }

    fun updatePasswordInput(text: String) {
        _passwordInput.value = text
    }

    fun clearErrorMessage(){
        _errorMessage.value = ""
    }

}

