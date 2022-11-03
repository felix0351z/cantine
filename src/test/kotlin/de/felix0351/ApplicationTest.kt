package de.felix0351

import de.felix0351.db.DatabaseService
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


const val EXAMPLE_USERNAME = "felix0351"
const val EXAMPLE_PASSWORD = "BanAne9!"

/*
Simulate a normal server start
 */
fun testModule(func: suspend ApplicationTestBuilder.(testClient: HttpClient) -> Unit) = testApplication {
    application {
        FileHandler.load()
        DatabaseService.init()

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
suspend fun HttpClient.login() = submitForm(
    url = "/login",
    formParameters = Parameters.build {
        append("username", EXAMPLE_USERNAME)
        append("password", EXAMPLE_PASSWORD)
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



