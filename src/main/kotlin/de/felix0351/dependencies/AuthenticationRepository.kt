package de.felix0351.dependencies

import de.felix0351.models.objects.User


interface AuthenticationRepository {

    suspend fun getUserByUsername(username: String): User?

    suspend fun getUsers(user: User): List<User>

    suspend fun addUser(user: User): Boolean

    suspend fun removeUser(user: User): Boolean


}