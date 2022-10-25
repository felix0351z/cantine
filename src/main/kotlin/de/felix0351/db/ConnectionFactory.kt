package de.felix0351.db

import de.felix0351.exceptions.MissingArgumentsException
import de.felix0351.exceptions.WrongSQLTypeException
import de.felix0351.models.ConfigFile

object ConnectionFactory {

    @Throws(WrongSQLTypeException::class, MissingArgumentsException::class)
    fun fromConfig(config: ConfigFile): Connection {
        when(config.database.type) {
            "sqlite" -> {
                return Connection.SQLite(config.database.url)
            }

            "mysql" -> {
                // If the password or the username are null, then a connection won't be possible
                if (config.database.username == null || config.database.password == null) {
                    throw MissingArgumentsException()
                }

                return Connection.MySQL(
                    config.database.url,
                    config.database.username,
                    config.database.password
                )
            }


        }

        //If there is no correct type throw an exception
        throw WrongSQLTypeException(config.database.type)
    }

}



sealed class Connection {
    class MySQL(val url: String, val username: String?, val password: String?) : Connection()
    class SQLite(val url: String) : Connection()
}
