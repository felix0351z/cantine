package de.felix0351.models

import kotlinx.serialization.Serializable
/**
 * Config file for the server, which will be read as yaml
 *
 * @property port Port, where the server will run on
 * @property database Properties for the connection to the database
 *
 */
@Serializable
data class ConfigFile(
    val port: Int,
    val database: DatabaseProperties,
    val authentication: AuthenticationProperties
)


/**
 * Properties for the database connection
 *
 * @property host Address to the database
 * @property port Standard mongodb port is 27017
 * @property database Name of the database
 * @property username Username for the database
 * @property password Password for the database
 * @property authMechanism Mechanism for the authentication (sha-256 or sha-1 normally)
 * @property timeout: ImeOut in Milliseconds
 */
@Serializable
data class DatabaseProperties(
    val host: String,
    val port: Int,
    val database: String,
    //val port: Int?,
    val username: String?,
    val password: String?,
    val authMechanism: String?,
    val timeout: Long?
)

/**
 * Properties for the user authentication
 * @property session_age How long a user can be signed in (in days)
 * @property sign_key  Key to sign every user session (Automatically generated)
 * @property auth_key Key to encrypt the cookies data
 * @property pepper
 *
 */
@Serializable
data class AuthenticationProperties(
    val session_age: Int,
    val sign_key: String,
    val auth_key: String,
    val pepper: String
)