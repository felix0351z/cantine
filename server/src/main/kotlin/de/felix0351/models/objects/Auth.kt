package de.felix0351.models.objects

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
import org.litote.kmongo.Id


sealed class Auth {

    /**
     * User
     *
     * @property username Unique name of the user
     * @property name The personal name
     * @property permissionLevel User's permission level
     * @property credit Prepaid credit of the user
     * @property hash Hashed password of the user
     *
     */

    data class User(
        @SerialName("_id") val id: Id<User>?,
        val username: String,
        val name: String,
        val permissionLevel: PermissionLevel,
        val credit: String,
        val hash: String
    )


    /**
     * A Payment order from the past
     *
     * @property title Name of the order
     * @property price Price
     * @property creationTime Creation Time of the payment
     *
     */
    data class Payment(
        val title: String,
        val price: Float,
        val creationTime: Long
    )

    enum class PermissionLevel(level: Int) {
        ADMIN(2),
        WORKER(1),
        USER(0)
    }

    data class UserSession(
        val id: ObjectId,
    ): Principal




}

