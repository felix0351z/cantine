package de.felix0351


import de.felix0351.plugins.configureDependencyInjection
import de.felix0351.plugins.configureRouting
import de.felix0351.plugins.configureSecurity
import de.felix0351.plugins.configureSerialization
import de.felix0351.utils.FileHandler
import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.testing.*


const val EXAMPLE_USERNAME = "admin"
const val EXAMPLE_PASSWORD = "cantine"

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
Simulate login
 */
suspend fun HttpClient.login(password: String = EXAMPLE_PASSWORD) = submitForm(
    url = "/login",
    formParameters = Parameters.build {
        append("username", EXAMPLE_USERNAME)
        append("password", password)
    }
) {
    https()
}

/*
Simulate logout
 */
suspend fun HttpClient.logout() = get("/user/logout") {
    https()
}



