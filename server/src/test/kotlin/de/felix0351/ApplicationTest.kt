package de.felix0351


import de.felix0351.plugins.configureDependencyInjection
import de.felix0351.plugins.configureRouting
import de.felix0351.plugins.configureSecurity
import de.felix0351.plugins.configureSerialization
import de.felix0351.utils.FileHandler
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json


const val EXAMPLE_USERNAME = "admin"
const val EXAMPLE_PASSWORD = "admin"

/*
Simulate a normal server start
 */
fun testModule(func: suspend ApplicationTestBuilder.(testClient: HttpClient) -> Unit) = testApplication {
    application {
        FileHandler.load()

        configureDependencyInjection()
        configureSecurity()
        configureSerialization()
        configureRouting()
    }

    val testClient = client.config {
        // Install cookies to remember login session and work with them
        install(HttpCookies)
        // For serialization
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                }
            )
        }

    }

    func(testClient)
}


/*
Use https for calls
 */
fun HttpRequestBuilder.https() {
    url {
        protocol = URLProtocol.HTTPS
    }
}

/*
Set json as content-type
 */
inline fun<reified T> HttpRequestBuilder.json(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}


/*
Simulate login
 */
suspend fun HttpClient.login(username: String = EXAMPLE_USERNAME, password: String = EXAMPLE_PASSWORD) = submitForm(
    url = "/api/login",
    formParameters = Parameters.build {
        append("username", username)
        append("password", password)
    }
) {
    https()
}

/*
Simulate logout
 */
suspend fun HttpClient.logout() = get("/api/logout") {
    https()
}



