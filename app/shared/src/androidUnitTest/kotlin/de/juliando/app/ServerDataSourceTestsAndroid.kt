package de.juliando.app

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.Test
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import kotlin.test.assertEquals

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    engine {
        config {
            sslSocketFactory(getSslContext().socketFactory, TrustAllX509TrustManager())
            hostnameVerifier { p0, p1 -> true }
        }
    }
}

fun getSslContext(): SSLContext {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(TrustAllX509TrustManager()), null)
    return sslContext
}

class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = emptyArray()
    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}


class ServerDataSourceTests {


    @Test
    fun testConnection() = testModule {
        val response = it.get(SERVER_TEST_URL) {https()}

        assertEquals(response.status, HttpStatusCode.OK)
        println(response.bodyAsText())
    }

    @Test
    fun testLogin() = testModule {
        val response = it.login("admin", "admin")

        assertEquals(response.status, HttpStatusCode.Accepted)
        println(response.bodyAsText())
    }


}