package de.juliando.app.repository

import de.juliando.app.data.LocalDataStoreImpl
import de.juliando.app.data.ServerDataSourceImpl
import de.juliando.app.models.objects.*

class AuthenticationRepositoryImpl(
    private val server: ServerDataSourceImpl = ServerDataSourceImpl(),
    private val cache: LocalDataStoreImpl = LocalDataStoreImpl()
) : AuthenticationRepository {

    override suspend fun getAccount(): Auth.User {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(request: PasswordChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): List<Auth.User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(username: String): List<Auth.User> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(request: UserAddRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(request: UserDeleteRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserName(request: NameChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserPassword(request: PasswordChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserPermission(request: PermissionChangeRequest) {
        TODO("Not yet implemented")
    }
}