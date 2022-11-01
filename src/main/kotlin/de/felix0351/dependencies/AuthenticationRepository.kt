package de.felix0351.dependencies

import de.felix0351.db.query
import de.felix0351.models.objects.User


interface AuthenticationRepository {

    suspend fun getUserByUsername(username: String): User?

    suspend fun getUsers(): List<User>

    suspend fun addUser(user: User)

    suspend fun removeUser(username: String)

    suspend fun <T> withTransaction(func: suspend AuthenticationRepository.() -> T ): T = query {
        func()
    }

}