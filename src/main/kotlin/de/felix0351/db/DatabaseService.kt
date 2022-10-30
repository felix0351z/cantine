package de.felix0351.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.felix0351.models.ConfigFile
import de.felix0351.utils.fail
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseService {
    
    fun init(config: ConfigFile) {
        try {
            val type = ConnectionFactory.fromConfig(config)
            val db = when(type) {
                is Connection.MySQL -> connectHikari(type)
                is Connection.SQLite -> connectSQLite(type)
            }

            transaction {
                //TODO Create tables and initialize them
            }


        } catch (e: Exception) {
            // WrongSQLTypeConnection | MissingArgumentsException | HikariPoolInitializationException | and from exposed can be thrown
            fail(e)
        }
        
    }

    private fun connectSQLite(properties: Connection.SQLite): Database {
        return Database.connect("jdbc:sqlite:/${properties.url}")
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