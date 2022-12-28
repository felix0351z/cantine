package de.felix0351.dependencies

import de.felix0351.models.objects.Auth
import de.felix0351.utils.Hashing

class CantineService(
    private val authRepo: AuthenticationRepository,
    private val contentRepo: ContentRepository
    ) {


    suspend fun checkUserCredentials(username: String, password: String): Auth.User? {

        val user = authRepo.getUserByUsername(username) ?: return null
        val correct =  Hashing.checkPassword(
            pw = password,
            hash = user.hash
        )

        return if (correct) user else null
    }






}