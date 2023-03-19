package de.juliando.app

import de.juliando.app.data.createHttpClient
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

const val SERVER_TEST_URL = "https://185.215.180.245"

/**
 * Creates a test module with the normal pre configured httpclient
 */
fun testModule(func: suspend (client: HttpClient) -> Unit) = runBlocking {
    func(createHttpClient())
}

/**
 * Test method for a login
 */
suspend fun HttpClient.login(username: String, password: String) = submitForm(
    url = "$SERVER_TEST_URL/login",
    formParameters = Parameters.build {
        append("username", username)
        append("password", password)
    }
)


