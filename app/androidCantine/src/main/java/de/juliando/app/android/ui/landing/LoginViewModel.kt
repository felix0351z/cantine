package de.juliando.app.android.ui.landing

import androidx.lifecycle.ViewModel
import de.juliando.app.data.LocalDataStore
import de.juliando.app.models.errors.HttpStatusException
import de.juliando.app.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {

    private var _serverURLInput: MutableStateFlow<String> = MutableStateFlow("http://207.180.215.119:8080/api")
    val serverURLInput = _serverURLInput.asStateFlow()

    private var _usernameInput: MutableStateFlow<String> = MutableStateFlow("")
    val usernameInput = _usernameInput.asStateFlow()

    private var _passwordInput: MutableStateFlow<String> = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()

    private var _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    suspend fun signIn(): Boolean{
        return try {
            val url = serverURLInput.value
            if (url==""){
                _errorMessage.value = "Bitte gebe eine URL ein!"
                false
            }else {
                // Stores the url
                LocalDataStore.storeURL(url)
                // Try to login with the usernameInput and passwordInput
                authenticationRepository.login(usernameInput.value, passwordInput.value)
                // Deletes the old user in storage
                LocalDataStore.storeCurrentUser(null)
                // Stores the current user
                LocalDataStore.storeCurrentUser(authenticationRepository.getAccount())
                true
            }
        }catch (e: HttpStatusException.UnauthorizedException){
            _errorMessage.value = "Benutzername und Passwort stimmen nicht überein!"
            _passwordInput.value = ""
            false
        }catch (e: Exception){
            _errorMessage.value = "Die URL ist falsch!"
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

