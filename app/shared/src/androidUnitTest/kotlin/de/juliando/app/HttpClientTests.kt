package de.juliando.app

import de.juliando.app.data.setAuthenticationCookie
import de.juliando.app.models.objects.backend.Auth
import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.backend.CreateOrderRequest
import de.juliando.app.models.objects.backend.CreateOrderRequestMeal
import de.juliando.app.repository.AuthenticationRepositoryImpl
import de.juliando.app.repository.PaymentRepositoryImpl
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class ServerDataSourceTests {

    @Test
    fun testConnection() = testModule {
        val response = it.get("http://207.180.215.119:8080/api")

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
    fun testCookieAccess() = testModule {
        val cookie = it.login("admin", "admin").setCookie()[0]
        val cookieInStr = renderCookieHeader(cookie)
        val cookie2 = parseServerSetCookieHeader(cookieInStr)

        val response = it.get("$SERVER_TEST_URL/content/meals") {
            headers.append(HttpHeaders.Cookie, renderCookieHeader(cookie2))
        }

        assertEquals(response.status, HttpStatusCode.OK)
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
            creationTime = null,
            tags = emptyList()
        )

        val cookie = it.login("admin", "admin").setCookie()[0]

        val request = it.post("$SERVER_TEST_URL/content/report") {
            setBody(MultiPartFormDataContent(
                parts = formData {
                    append("image", File(testImage).readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"burger.jpeg\"")
                    })
                    append(FormPart("json", Json.encodeToString(report), Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }

            ))
            headers.append(HttpHeaders.Cookie, renderCookieHeader(cookie))

        }
        println(request.bodyAsText())
        assertEquals(request.status, HttpStatusCode.OK)
    }

    @Test
    fun addMealTest() = testModule {
        val testImage = "C:\\Users\\felix\\Downloads\\schnitzel.jpg"
        val meal = Content.Meal(
            id = null,
            category = "Wochenplan",
            name = "Schnitzel mit Pommes oder Spätzle",
            description = "Schwein-, Hähnchen- und Gemüseschnitzel zur Auswahl!",
            price = 12.0F,
            deposit = 5F,
            day = "Donnerstag",
            selections = emptyList(),
            picture = null,
            tags = emptyList()
        )
        val str = Json.encodeToString(meal)

        val cookie = it.login("admin", "admin").setCookie()[0]

        val request = it.post("$SERVER_TEST_URL/content/meal") {
            setBody(MultiPartFormDataContent(
                parts = formData {
                    append("image", File(testImage).readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"meal.jpeg\"")
                    })
                    append(FormPart("json", str, Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }

            ))
            headers.append(HttpHeaders.Cookie, renderCookieHeader(cookie))

        }
        println(request.bodyAsText())
        assertEquals(request.status, HttpStatusCode.OK)



    }

    @Test
    fun createOrderRequest() = testModule {
        val order = CreateOrderRequestMeal(
            id = "6446bfd8393dd84c5d25c1f0",
            selections = emptyList()
        )

        val orderRequest = CreateOrderRequest(
            meals = listOf(order)
        )

        val cookie = it.login("admin", "admin").setCookie()[0]

        val request = it.post("$SERVER_TEST_URL/payment/order") {
            setBody(orderRequest)
            headers.append(HttpHeaders.Cookie, renderCookieHeader(cookie))
        }

        println(request.bodyAsText())
        assertEquals(request.status, HttpStatusCode.OK)

    }


}