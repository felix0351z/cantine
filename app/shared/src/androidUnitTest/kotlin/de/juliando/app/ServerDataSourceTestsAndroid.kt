package de.juliando.app

import de.juliando.app.models.objects.Content
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.io.File
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
        val response = it.get(SERVER_TEST_URL)

        assertEquals(response.status, HttpStatusCode.OK)
        println(response.bodyAsText())
    }

    @Test
    fun testLogin() = testModule {
        val response = it.login("admin", "admin")

        assertEquals(response.status, HttpStatusCode.Accepted)
        println(response.bodyAsText())
    }

    @Test
    fun testPictureUpload() = testModule {
        val testImage = "C:\\Users\\felix\\Downloads\\burger.jpg"
        val report = Content.Report(
            id = null,
            title = "Neuer Burder jetzt verfügbar!",
            description = "Ab dem 14.03 kann ein neuer veganer Burger bei uns geholt werden!",
            picture = null,
            creationTime = null
        )

        it.login("admin", "admin")

        val request = it.post("$SERVER_TEST_URL/content/report") {
            setBody(MultiPartFormDataContent(
                parts = formData {
                    append("image", File(testImage).readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"test-file.jpg\"")
                    })
                    append(FormPart("json", Json.encodeToString(report), Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }

            ))

        }
        println(request.bodyAsText())
        assertEquals(request.status, HttpStatusCode.OK)
    }


}