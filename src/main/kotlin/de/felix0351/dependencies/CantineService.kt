package de.felix0351.dependencies

import de.felix0351.db.query

class CantineService(
    private val authRepo: AuthenticationRepository,
    private val contentRepo: ContentRepository
    ) {


    suspend fun checkUserCredentials(username: String, password: String): Boolean = query {
        val user = authRepo.getUserByUsername(username) ?: return@query false

        user.password == password
    }






}