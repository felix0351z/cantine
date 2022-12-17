package de.felix0351

import de.felix0351.db.MongoDBConnection
import de.felix0351.utils.FileHandler
import org.junit.Test

class DatabaseTests {

    @Test
    fun testConnection() {
        FileHandler.load()
        MongoDBConnection()




    }


}