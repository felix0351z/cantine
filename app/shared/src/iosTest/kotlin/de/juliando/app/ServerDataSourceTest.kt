package de.juliando.app

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {

}

class ServerDataSourceTest {




}