package de.felix0351

import de.felix0351.db.MongoDBConnection
import de.felix0351.dependencies.AuthenticationRepository
import de.felix0351.dependencies.AuthenticationRepositoryMongoDB
import de.felix0351.dependencies.ContentRepository
import de.felix0351.dependencies.ContentRepositoryMongoDB
import de.felix0351.utils.FileHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

fun getConnection(): MongoDBConnection {
    FileHandler.load()
    return MongoDBConnection()
}

fun getAuthRepo(): AuthenticationRepository {
    return AuthenticationRepositoryMongoDB(getConnection())
}

fun getContentRepo(): ContentRepository {
    return ContentRepositoryMongoDB(getConnection())
}

class DatabaseTests {


    @Test
    fun testConnection() {
        getConnection()
    }

    // Configure the username and password in config.yaml
    @Test
    fun testAuthorization() {
        val con = getConnection()

        val lists = runBlocking {
            con.callToDatabase {
                it.listCollectionNames()
            }
        }
        println(lists)

    }

   @Test
   fun testTimeOut() {
       val con = getConnection()

       runBlocking {
           con.callToDatabase { it.listCollectionNames() }
           println("Call 1 executed")
           delay(15*1000)

           // Disconnect lan manually
           con.callToDatabase { it.listCollectionNames() }
       }

   }

}