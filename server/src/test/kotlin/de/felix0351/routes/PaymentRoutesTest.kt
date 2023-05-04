package de.felix0351.routes

import de.felix0351.*
import de.felix0351.models.objects.AddCreditRequest
import de.felix0351.models.objects.Auth
import de.felix0351.objects.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder

import kotlin.test.Test

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentRoutesTest {

    companion object {
        var orderID = ""
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    fun testAddCredit() = testModule {
        it.login()

        val request = AddCreditRequest(
            password = EXAMPLE_PASSWORD,
            username = EXAMPLE_USERNAME, // Geld eigenem Benutzer geben
            credit = 20F
        )

        val response = it.post("/api/payment/credit") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    fun testGetCredit() = testModule {
        it.login()

        val response = it.get("/api/payment/credit") {
            https()
        }

        println(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }


    @Test
    @org.junit.jupiter.api.Order(3)
    fun testGetOrders() = testModule {
        it.login()

        val response = it.get("/api/payment/orders") {
            https()
        }

        println(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    fun testAddOrder() = testModule {
        it.login()

        val meals: List<Meal> = it.get("/api/content/meals") {
            https()
        }.body()

        val request = CreateOrderRequest(
            meals = listOf(
                CreateOrderRequestMeal(
                    id = meals[0].id!!,
                    selections = listOf("Ketchup", "Senf")
            ))
        )

        val response = it.post("/api/payment/order") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        orderID = response.body<Order>().id
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    fun testPurchase() = testModule {
        it.login()

        val request = VerifyOrderRequest(
            username = EXAMPLE_USERNAME,
            orderId = orderID
        )

        val response = it.post("/api/payment/purchase") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    fun testDeleteOrder() = testModule {
        it.login()

        val response = it.delete("/api/payment/order") {
            https()
            header("id", orderID)
            //json(orderID)
        }

        println(response.bodyAsText())

        //NOT BECAUSE THERE WILL BE NO ORDER TO DELETE
        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    fun testGetPurchases() = testModule {
        it.login()

        val response = it.get("/api/payment/purchases") {
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
        println(response.body<List<Auth.Payment>>())
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    fun testCleanPurchases() = testModule {
        it.login()

        val response = it.delete("/api/payment/purchases") {
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }













}