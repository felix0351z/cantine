package de.juliando.app.repository

import de.juliando.app.models.objects.*
import de.juliando.app.models.objects.backend.*

/**
 * This repository handles the authentication data.
 */

interface AuthenticationRepository {

    //Login/Logout
    @Throws(Exception::class)
    suspend fun login(username: String, password: String)
    @Throws(Exception::class)
    suspend fun logout()

    //Account
    @Throws(Exception::class)
    suspend fun getAccount(): Auth.User
    @Throws(Exception::class)
    suspend fun changePassword(request: PasswordChangeRequest)

    //User
    @Throws(Exception::class)
    suspend fun getUsers(): List<Auth.User>
    @Throws(Exception::class)
    suspend fun getUser(username: String): List<Auth.User>
    @Throws(Exception::class)
    suspend fun addUser(request: UserAddRequest)
    @Throws(Exception::class)
    suspend fun deleteUser(request: UserDeleteRequest)
    @Throws(Exception::class)
    suspend fun changeUserName(request: NameChangeRequest)
    @Throws(Exception::class)
    suspend fun setUserPassword(request: PasswordChangeRequest)
    @Throws(Exception::class)
    suspend fun changeUserPermission(request: PermissionChangeRequest)

}