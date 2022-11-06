package de.felix0351.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.felix0351.models.tables.Meals
import de.felix0351.models.tables.News
import de.felix0351.models.tables.UserSessions
import de.felix0351.models.tables.Users
import de.felix0351.utils.FileHandler
import de.felix0351.utils.fail
import de.felix0351.utils.getLogger
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseService {

    private val logger = getLogger()

    private val TABLES = listOf(
        Users, UserSessions,
        News,
        Meals.Categories, Meals.Meals, Meals.Orders, Meals.SelectionGroups
        )
    
    fun init() {
        try {
            val type = ConnectionFactory.fromConfig(FileHandler.configuration)
            val db = when(type) {
                is Connection.MySQL -> connectHikari(type)
                is Connection.SQLite -> connectSQLite(type)
            }

            transaction {
                // Erstelle alle benötigten Tabellen für die Repos
                TABLES.forEach { table ->
                    SchemaUtils.create(table)
                }
                logger.info("Loaded database tables successfully")
            }


        } catch (e: Exception) {
            logger.error("Error while initiating database connection")
            // WrongSQLTypeConnection | MissingArgumentsException | HikariPoolInitializationException | and from exposed can be thrown
            fail(e)
        }
        
    }


    private fun connectSQLite(properties: Connection.SQLite): Database {
        return Database.connect("jdbc:sqlite:${properties.url}", "org.sqlite.JDBC")
    }


    private fun connectHikari(properties: Connection.MySQL): Database {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://${properties.url}"
            username = properties.username
            password = properties.password
        }

        val dataSource = HikariDataSource(config)
        return Database.connect(dataSource)
    }

}

/**
 * Easy access function to make an async sql statement
 */
suspend fun <T> query(statement: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO) {
        statement()
    }