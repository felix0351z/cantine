package de.juliando.app.data

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.engine.darwin.certificates.*

/**
 *  Returns a pre-configured ktor httpclient for the ios platform
 * @param config Config for all platforms
 *
 * See https://api.ktor.io/ktor-client/ktor-client-darwin/io.ktor.client.engine.darwin.certificates/-certificate-pinner/index.html
 */
actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    engine {
        val builder = CertificatePinner.Builder().add( "",  "")
        handleChallenge(builder.build())
    }

    //TODO: Configure SSL
    config()
}