package de.juliando.app.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 *  Returns a pre-configured ktor httpclient for the android platform
 * @param config Config for all platforms
 */
actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    engine {
        config {

            // Allow all untrusted certificates and hostnames
            //TODO: Only for development purpose. Need to be changed in production. A untrusted certificate could be a Man-In-The-Middle attack
            sslSocketFactory(getSslContext().socketFactory, TrustAllX509TrustManager())
            hostnameVerifier { _, _ -> true }
        }
    }

    config()
}

fun getSslContext(): SSLContext {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(TrustAllX509TrustManager()), null)
    return sslContext
}

/*
* X509TrustManager which allows all certificates
* Only for development purpose. Don't use in production
*/
class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = emptyArray()
    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}

