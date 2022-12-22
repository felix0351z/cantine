package de.felix0351

import de.felix0351.db.MongoDBConnection
import de.felix0351.dependencies.AuthenticationRepositoryMongoDB
import de.felix0351.models.errors.Response
import de.felix0351.models.objects.Auth
import de.felix0351.models.errors.ErrorCode
import de.felix0351.utils.FileHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class DatabaseTests {

    private fun getConnection(): MongoDBConnection {
        FileHandler.load()
        return MongoDBConnection()
    }

    @Test
    fun testConnection() {
        getConnection()
    }

    // Configure the username and password in config.yaml
    @Test
    fun testAuthorization() {
        val con = getConnection()

        val lists = runBlocking {
            con.call {
                it.listCollectionNames()
            }
        }
        println(lists)

    }

   @Test
   fun testTimeOut() {
       val con = getConnection()

       runBlocking {
           con.call { it.listCollectionNames() }
           println("Call 1 executed")
           delay(15*1000)

           // Disconnect lan manually
           con.call { it.listCollectionNames() }
       }

   }

    @Test
    fun addUser() = runBlocking {
        val user = Auth.User(
            id = null,
            username = "felix0351",
            name = "Felix Zimmermann",
            permissionLevel = Auth.PermissionLevel.ADMIN,
            credit = "fdgfgdf",
            hash = "blabla"

        )


        val con = getConnection()
        val repo = AuthenticationRepositoryMongoDB(con)

        val response = repo.addUser(user)
        assertEquals(response, Response.Ok)

    }

    @Test
    fun getUser() = runBlocking {
        val con = getConnection()
        val repo = AuthenticationRepositoryMongoDB(con)

        val user = repo.getUserByUsername("felix0351")

        assertNotEquals(user, null)
    }

    @Test
    fun updatePermissionStatus() = runBlocking {
        val con = getConnection()
        val repo = AuthenticationRepositoryMongoDB(con)

        val response = repo.updatePermissionLevel("felix0351", Auth.PermissionLevel.ADMIN)

        assertEquals(response, Response.Error(ErrorCode.SameValue))
    }

}