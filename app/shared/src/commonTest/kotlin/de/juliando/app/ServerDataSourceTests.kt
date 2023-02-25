package de.juliando.app

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

const val SERVER_TEST_URL = "https://185.215.180.245"



expect fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

fun testModule(func: suspend (client: HttpClient) -> Unit) = runBlocking {
    val client = httpClient {
        install(HttpCookies)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

    }

    func(client)
}

fun HttpRequestBuilder.https() =
    url { protocol = URLProtocol.HTTPS }

inline fun<reified T> HttpRequestBuilder.setJson(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}

suspend fun HttpClient.login(username: String, password: String) = submitForm(
    url = "$SERVER_TEST_URL/login",
    formParameters = Parameters.build {
        append("username", username)
        append("password", password)
    }
) { https() }

suspend fun HttpClient.logout() = get("/logout") { https() }


