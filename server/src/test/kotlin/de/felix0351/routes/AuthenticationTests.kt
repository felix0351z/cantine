package de.felix0351.routes

import de.felix0351.EXAMPLE_PASSWORD
import de.felix0351.login
import de.felix0351.logout
import de.felix0351.testModule
import de.felix0351.utils.FileHandler
import de.felix0351.utils.Hashing
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthenticationTests {

   @Test
   fun testBCrypt() {
       testUtils()

       val hash = Hashing.toHash(EXAMPLE_PASSWORD)

       assertTrue(Hashing.checkPassword(EXAMPLE_PASSWORD, hash))
   }

    @Test
    fun testUtils() {
        FileHandler.load()
        //DatabaseService.init()
    }

    @Test
    fun testRoot() = testModule {
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cantine Server is running", response.bodyAsText())
    }

    @Test
    fun testLogin() = testModule {
        val response = it.login()

        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun testLogout() = testModule {
        it.login()
        val response = it.logout()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Logout successfully", response.bodyAsText())
    }

    @Test
    fun testStatusPages() = testModule {
        val loginResponse = it.login("NichtRichtig")
        val logoutResponse = it.logout()

        assertEquals("401: Password or Username incorrect", loginResponse.bodyAsText())
        assertEquals("403: No valid session", logoutResponse.bodyAsText())
    }

}