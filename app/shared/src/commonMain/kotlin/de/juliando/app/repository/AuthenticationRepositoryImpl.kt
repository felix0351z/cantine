package de.juliando.app.repository

import de.juliando.app.data.LocalDataStore
import de.juliando.app.data.ServerDataSource
import de.juliando.app.data.StorageKeys
import de.juliando.app.models.objects.backend.*

/**
 * This repository handles the authentication data.
 * It uses the ServerDataSource to get the data from the server.
 */
//TODO: Save username and password in local datastore
class AuthenticationRepositoryImpl(
    private val server: ServerDataSource = ServerDataSource(),
) : AuthenticationRepository {

    override suspend fun login(username: String, password: String) = server.login(username, password)
    override suspend fun logout() = server.logout()

    override suspend fun getAccount(): Auth.User {
        return server.get("/account")
    }

    override suspend fun changePassword(request: PasswordChangeRequest) {
        server.post<PasswordChangeRequest, String>("/account/password", request)
    }

    override suspend fun getUsers(): List<Auth.User> {
        return server.getList("/users")
    }

    override suspend fun getUser(username: String): List<Auth.User> {
        return server.get("/user", username)
    }

    override suspend fun addUser(request: UserAddRequest) {
        server.post<UserAddRequest, String>("/user", request)
    }

    override suspend fun deleteUser(request: UserDeleteRequest) {
        TODO()
        // Requests can't be delivered over delete method in the body, must be changed in future
        //server.delete<UserDeleteRequest, String>("/content/meal", request)
    }

    override suspend fun changeUserName(request: NameChangeRequest) {
        server.post<NameChangeRequest, String>("/user/name", request)
    }

    override suspend fun setUserPassword(request: PasswordChangeRequest) {
        server.post<PasswordChangeRequest, String>("/user/password", request)
    }

    override suspend fun changeUserPermission(request: PermissionChangeRequest) {
        server.post<PermissionChangeRequest, String>("/user/permission", request)
    }
}