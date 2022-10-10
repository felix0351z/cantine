package de.felix0351.db

import com.zaxxer.hikari.pool.HikariPool
import de.felix0351.models.ConfigFile
import de.felix0351.models.DatabaseProperties

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


internal class ConnectionTests {



    @Test
    fun fromConfig() {
        //Test the Connection factory
        val config = ConfigFile(
            port = 8080,
            databaseType = "sqlite",
            DatabaseProperties("data.db", null, null)
        )
        val type = Connection.SQLite("data.db")
        val result = ConnectionFactory.fromConfig(config)

        assertTrue(result is Connection.SQLite)

        assertEquals(result.url, type.url)
    }

    @Test()
    fun connectDatabase() {
        val config = ConfigFile(
            port = 8080,
            databaseType = "mysql",
            databaseProperties = DatabaseProperties("fdggfdd", "ggfdgfdg", "gdghfhg")
        )

        assertFailsWith(HikariPool.PoolInitializationException::class) {
            DatabaseService.init(config)
        }
    }

}