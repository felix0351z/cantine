package de.felix0351.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * User-Table which will be stored in the database
 *
 * @property id Unique id of the user, which is also be the primary key
 * @property name The personal name
 * @property mail E-Mail address of the user
 * @property permissionLevel User's permission level
 * @property hash Hashed password with salt and pepper
 *
 */
object Users : Table("users") {

    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 128)
    val mail: Column<String> = varchar("mail", 128)
    val permissionLevel: Column<Int> = integer("permission_level")
    val hash: Column<ByteArray> = binary("hash", 60)

    override val primaryKey = PrimaryKey(id)
}

/**
 *  Saves all opened sessions
 *
 * @property id Unique id of the session. Automatically generated.
 * @property value Content of the session
 */
object UserSessions : Table("sessions") {

    val id: Column<String> = varchar("session_id", 128).autoIncrement()
    val value: Column<String> = varchar("session_value", 512)

    override val primaryKey = PrimaryKey(id)
}