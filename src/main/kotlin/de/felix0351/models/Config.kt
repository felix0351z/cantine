package de.felix0351.models

import kotlinx.serialization.Serializable

/**
 * Config file for the server, which will be read as yaml
 *
 * @property port Port, where the server will run on
 * @property databaseType The name, which database should be used (mysql, mariadb, or sqlite)
 * @property databaseProperties Properties for the connection to the database
 *
 */
@Serializable
data class ConfigFile(
    val port: Int,
    val databaseType: String,
    val databaseProperties: DatabaseProperties
)


/**
 * Properties for the database connection
 *
 * @property url Adress to the database
 * //@property port Standard sql port is 3306
 * @property username Username for the database
 * @property password Password for the database
 */
@Serializable
data class DatabaseProperties(
    val url: String,
    //val port: Int?,
    val username: String?,
    val password: String?
)