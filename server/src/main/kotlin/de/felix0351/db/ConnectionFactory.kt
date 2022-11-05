package de.felix0351.db


import de.felix0351.models.ConfigFile
import de.felix0351.utils.SQLException
import de.felix0351.utils.fail

object ConnectionFactory {

    @Throws(SQLException.WrongSQLTypeException::class, SQLException.MissingArgumentsException::class)
    fun fromConfig(config: ConfigFile): Connection {
        when(config.database.type) {
            "sqlite" -> {
                return Connection.SQLite(config.database.url)
            }

            "mysql" -> {
                // If the password or the username are null, then a connection won't be possible
                if (config.database.username == null || config.database.password == null) {
                    fail(SQLException.MissingArgumentsException())
                }

                return Connection.MySQL(
                    config.database.url,
                    config.database.username,
                    config.database.password
                )
            }


        }

        //If there is no correct type throw an exception
        fail(SQLException.WrongSQLTypeException(config.database.type))
    }

}



sealed class Connection {
    class MySQL(val url: String, val username: String?, val password: String?) : Connection()
    class SQLite(val url: String) : Connection()
}
