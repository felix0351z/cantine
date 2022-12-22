package de.felix0351.dependencies

import de.felix0351.models.errors.Response
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Auth.User


interface AuthenticationRepository {

    suspend fun getUserByUsername(username: String): User?

    suspend fun getUsers(): List<User>

    suspend fun addUser(user: User): Response

    suspend fun removeUser(username: String): Response

    suspend fun updatePermissionLevel(username: String, level: Auth.PermissionLevel): Response

    suspend fun updateUserHash(username: String, hash: String): Response

    suspend fun updateUserCredit(username: String, hash: String): Response



}