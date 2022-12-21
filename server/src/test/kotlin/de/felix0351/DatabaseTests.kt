package de.felix0351

import de.felix0351.db.MongoDBConnection
import de.felix0351.utils.FileHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

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


}