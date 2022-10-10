package de.felix0351.db

import de.felix0351.exceptions.MissingArgumentsException
import de.felix0351.exceptions.WrongSQLTypeException
import de.felix0351.models.ConfigFile

object ConnectionFactory {

    @Throws(WrongSQLTypeException::class, MissingArgumentsException::class)
    fun fromConfig(config: ConfigFile): Connection {
        when(config.databaseType) {
            "sqlite" -> {
                return Connection.SQLite(config.databaseProperties.url)
            }

            "mysql" -> {
                // If the password or the username are null, then a connection won't be possible
                if (config.databaseProperties.username == null || config.databaseProperties.password == null) {
                    throw MissingArgumentsException()
                }

                return Connection.MySQL(
                    config.databaseProperties.url,
                    config.databaseProperties.username,
                    config.databaseProperties.password
                )
            }


        }

        //If there is no correct type throw an exception
        throw WrongSQLTypeException(config.databaseType)
    }

}



sealed class Connection {
    class MySQL(val url: String, val username: String?, val password: String?) : Connection()
    class SQLite(val url: String) : Connection()
}
