package de.felix0351.models.objects

import io.ktor.server.auth.*



/**
 * User
 *
 * @property username Unique name of the user
 * @property name The personal name
 * @property permissionLevel User's permission level
 * @property hash Hashed password of the user
 *
 */
data class User(
    val username: String,
    val name: String,
    val permissionLevel: Int,
    val hash: String
)

data class UserSession(
    val username: String,
): Principal
