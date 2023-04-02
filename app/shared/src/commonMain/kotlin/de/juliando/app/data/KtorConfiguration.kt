package de.juliando.app.data

import de.juliando.app.models.errors.NoSessionException
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

/**
 *  Returns a pre-configured ktor httpclient for all platforms
 * @param config Config for all platforms
 */
expect fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

/**
 * Create a new ktor-client with installed content negotiation
 */
@OptIn(ExperimentalSerializationApi::class)
fun createHttpClient() = httpClient {
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
            encodeDefaults = true
        })
    }

}

/**
 * Set a http-body with the content type application/json
 */
inline fun<reified T: Any> HttpRequestBuilder.setJsonBody(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}

/**
 * Sets the cached authentication cookie for the request
 * @throws NoSessionException If no authentication cookie will be found
 */
fun HttpRequestBuilder.setAuthenticationCookie() {
    val cookie = LocalDataStore.getAuthenticationCookieHeader()

    if (cookie != null) headers.append(HttpHeaders.Cookie, cookie)
    else throw NoSessionException("No authentication cookie found!")
}