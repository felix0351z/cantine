package de.felix0351.dependencies

import de.felix0351.db.query
import de.felix0351.utils.Hashing

class CantineService(
    private val authRepo: AuthenticationRepository,
    private val contentRepo: ContentRepository
    ) {


    suspend fun checkUserCredentials(username: String, password: String): Boolean = query {
        val user = authRepo.getUserByUsername(username) ?: return@query false

        Hashing.checkPassword(
            pw = password,
            hash = user.hash
        )
    }






}