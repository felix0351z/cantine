package de.felix0351.repositories

import de.felix0351.getAuthRepo
import de.felix0351.models.objects.Auth
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotEquals
import java.time.Instant
class AuthRepository {


    @Test
    fun addUser() = runBlocking {
        val user = Auth.User(
            username = "felix0351",
            name = "Felix Zimmermann",
            permissionLevel = Auth.PermissionLevel.ADMIN,
            credit = "fdgfgdf",
            hash = "blabla"

        )

        val repo = getAuthRepo()
        repo.addUser(user)
    }

    @Test
    fun getUser() = runBlocking {
        val repo = getAuthRepo()
        val user = repo.getUserByUsername("felix0351")

        assertNotEquals(user, null)
    }

    @Test
    fun updatePermissionStatus() = runBlocking {
        val repo = getAuthRepo()

        repo.updatePermissionLevel("felix0351", Auth.PermissionLevel.ADMIN)
    }

    @Test
    fun addPayment() = runBlocking {
        val payment = Auth.Payment(
            user = "felix0351",
            meals = listOf("Pommes mit Rahmsoße"),
            price = 2.2F,
            creationTime = Instant.now()
        )
        val repo = getAuthRepo()

        //repo.addPayment(payment)

    }


}