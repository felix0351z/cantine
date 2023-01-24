package de.felix0351.repository

import de.felix0351.models.errors.DatabaseException.*
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Auth.User

import kotlin.jvm.Throws


interface AuthenticationRepository {

    //User

    suspend fun getUserByUsername(username: String): User?

    suspend fun getUsers(): List<User>

    @Throws(ValueAlreadyExistsException::class)
    suspend fun addUser(user: User)

    @Throws(NotFoundException::class)
    suspend fun removeUser(username: String)

    @Throws(NotFoundException::class, SameValueException::class)
    suspend fun updatePermissionLevel(username: String, level: Auth.PermissionLevel)

    @Throws(NotFoundException::class)
    suspend fun updateUserHash(username: String, hash: String)

    @Throws(NotFoundException::class)
    suspend fun updateUserCredit(username: String, hash: String)

    @Throws(NotFoundException::class)
    suspend fun updateUserName(username: String, name: String)



}