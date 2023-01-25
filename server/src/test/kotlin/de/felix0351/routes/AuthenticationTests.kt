package de.felix0351.routes

import de.felix0351.*
import de.felix0351.models.objects.*
import de.felix0351.utils.FileHandler
import de.felix0351.utils.Hashing
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.Test

@TestMethodOrder(OrderAnnotation::class)
class AuthenticationTests {

    companion object {
        var testUser = Auth.PublicUser(
            username = "hanss",
            name = "Hans Peter",
            permissionLevel = Auth.PermissionLevel.ADMIN,
            credit = 10F,
            password = "SicheresPassword"
        )
    }

   @Test
   @Order(1)
   fun testBCrypt() {
       testUtils()

       val hash = Hashing.toHash(EXAMPLE_PASSWORD)

       assertTrue(Hashing.checkPassword(EXAMPLE_PASSWORD, hash))
   }

    @Test
    @Order(2)
    fun testUtils() {
        FileHandler.load()
        //DatabaseService.init()
    }

    @Test
    @Order(3)
    fun testRoot() = testModule {
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cantine Server is running", response.bodyAsText())
    }

    @Test
    @Order(4)
    fun testLogin() = testModule {
        val response = it.login()

        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    @Order(5)
    fun testLogout() = testModule {
        it.login()
        val response = it.logout()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Logout successfully", response.bodyAsText())
    }

    @Test
    @Order(6)
    fun testLoginFail() = testModule {
        val loginResponse = it.login("NichtRichtig", "hgfgfhhg")
        val logoutResponse = it.logout()

        assertEquals(HttpStatusCode.Unauthorized, loginResponse.status)
        assertEquals(HttpStatusCode.Forbidden, logoutResponse.status)
    }

    @Test
    @Order(7)
    fun testAddUser() = testModule {
        it.login()

        val request = UserAddRequest(
            password = EXAMPLE_PASSWORD,
            user = testUser
        )

        val response = it.post("/user") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(8)
    fun testGetUsers() = testModule {
        it.login()

        val list: List<Auth.PublicUser> = it.get("/users") {
            https()
        }.body()

        println(list)
    }

    @Test
    @Order(9)
    fun testGetAccount() = testModule {
        it.login()

        val self: Auth.PublicUser = it.get("/account") {
            https()
        }.body()

        println(self)
    }

    @Test
    @Order(10)
    fun testGetUser() = testModule {
        it.login()

        val user: Auth.PublicUser = it.get("/user/${testUser.username}") {
            https()
        }.body()

        println(user)
    }

    @Test
    @Order(11)
    fun testChangeOwnPassword() = testModule {
        it.login(username = testUser.username, password = testUser.password!!)
        val newPassword = "NeuesPasswort"

        val request = PasswordChangeRequest(
            username = null,
            password = testUser.password!!,
            newPassword = newPassword
        )
        val response =it.post("/account/password") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        testUser = testUser.copy(password = newPassword)
    }

    @Test
    @Order(12)
    fun testChangeUserName() = testModule {
        it.login()

        val request = NameChangeRequest(
            EXAMPLE_PASSWORD,
            testUser.username,
            "Günter Hans"
        )

        val response = it.post("/user/name") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(13)
    fun testChangeUserPassword() = testModule {
        it.login()

        val request = PasswordChangeRequest(
            EXAMPLE_PASSWORD,
            testUser.username,
            "123456passwort"
        )

        val response = it.post("/user/password") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(14)
    fun testChangeUserPermission() = testModule {
        it.login()

        val request = PermissionChangeRequest(
            EXAMPLE_PASSWORD,
            testUser.username,
            Auth.PermissionLevel.WORKER
        )

        val response = it.post("/user/permission") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(15)
    fun testDeleteUser() = testModule {
        it.login()

        val request = UserDeleteRequest(
            EXAMPLE_PASSWORD,
            testUser.username
        )

        val response = it.delete("/user") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


}