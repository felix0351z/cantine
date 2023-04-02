package de.juliando.app.data

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

/**
 *  Returns a pre-configured ktor httpclient for the ios platform
 * @param config Config for all platforms
 */
actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    //TODO: Configure SSL
    config()
}