package de.felix0351.models.objects

import io.ktor.server.auth.*
import java.time.LocalDateTime


/**
 * User
 *
 * @property id Unique id of the user, Null if it isn't already in the database
 * @property name The personal name
 * @property mail E-Mail address of the user
 * @property permissionLevel User's permission level
 * @property password Entered password of the user (hashed in db)
 *
 */
data class User(
    val id: Int?,
    val name: String,
    val mail: String,
    val permissionLevel: Int,
    val password: String
)

data class UserSession(
    val id: Int,
    val time: LocalDateTime
): Principal
