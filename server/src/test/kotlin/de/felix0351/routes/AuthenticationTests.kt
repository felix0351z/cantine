package de.felix0351.routes

import de.felix0351.*
import de.felix0351.models.objects.Auth
import de.felix0351.utils.FileHandler
import de.felix0351.utils.Hashing
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthenticationTests {

   @Test
   fun testBCrypt() {
       testUtils()

       val hash = Hashing.toHash(EXAMPLE_PASSWORD)

       assertTrue(Hashing.checkPassword(EXAMPLE_PASSWORD, hash))
   }

    @Test
    fun testUtils() {
        FileHandler.load()
        //DatabaseService.init()
    }

    @Test
    fun testRoot() = testModule {
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Cantine Server is running", response.bodyAsText())
    }

    @Test
    fun testLogin() = testModule {
        val response = it.login()

        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun testLogout() = testModule {
        it.login()
        val response = it.logout()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Logout successfully", response.bodyAsText())
    }

    @Test
    fun testStatusPages() = testModule {
        val loginResponse = it.login("NichtRichtig")
        val logoutResponse = it.logout()

        assertEquals("401: Password or Username incorrect", loginResponse.bodyAsText())
        assertEquals("403: No valid session", logoutResponse.bodyAsText())
    }

    @Test
    fun testUsers() = testModule {
        it.login()

        val list: List<Auth.PublicUser> = it.get("/users") {
            https()
        }.body()

        println(list)
    }

    @Test
    fun testGetAccount() = testModule {
        it.login()

        val self: Auth.PublicUser = it.get("/account") {
            https()
        }.body()

        println(self)
    }

    @Test
    fun testChangeOwnPassword() = testModule {
        it.login()

        val request = Auth.PasswordChangeRequest(
            username = null,
            password = EXAMPLE_PASSWORD,
            newPassword = EXAMPLE_PASSWORD
        )

        val response =it.post("/account/password") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testAddUser() = testModule {
        it.login()


        val request = Auth.UserAddRequest(
            password = EXAMPLE_PASSWORD,
            user = Auth.PublicUser(
                username = "hanss",
                name = "Hans Peter",
                permissionLevel = Auth.PermissionLevel.ADMIN,
                credit = 10F,
                password = "SicheresPassword"
            )
        )

        val response = it.post("/user") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetUser() = testModule {
        it.login()
        val username = "hanss"

        val user: Auth.PublicUser = it.get("/user/$username") {
            https()
        }.body()

        println(user)
    }

    @Test
    fun testDeleteUser() = testModule {
        it.login()

        val request = Auth.UserDeleteRequest(
            EXAMPLE_PASSWORD,
            "hanss"
        )

        val response = it.delete("/user") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testChangeUserName() = testModule {
        it.login()

        val request = Auth.NameChangeRequest(
            EXAMPLE_PASSWORD,
            "hanss",
            "Günter Hans"
        )

        val response = it.post("/user/name") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testChangeUserPassword() = testModule {
        it.login()

        val request = Auth.PasswordChangeRequest(
            EXAMPLE_PASSWORD,
            "hanss",
            "123456passwort"
        )

        val response = it.post("/user/password") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testChangeUserPermission() = testModule {
        it.login()

        val request = Auth.PermissionChangeRequest(
            EXAMPLE_PASSWORD,
            "hanss",
            Auth.PermissionLevel.WORKER
        )

        val response = it.post("/user/permission") {
            https()
            json(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


}