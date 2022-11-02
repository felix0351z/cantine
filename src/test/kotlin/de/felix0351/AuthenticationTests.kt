package de.felix0351

import de.felix0351.db.DatabaseService
import de.felix0351.utils.FileHandler
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationTests {

    @Test
    fun testUtils() {
        FileHandler.load()
        DatabaseService.init()
    }

    @Test
    fun testRoot() = testModule {
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cantine Server is running", response.bodyAsText())
    }

}