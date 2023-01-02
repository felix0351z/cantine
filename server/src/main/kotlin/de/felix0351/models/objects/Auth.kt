package de.felix0351.models.objects

import de.felix0351.utils.InstantSerializer
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.time.Instant


sealed class Auth {

    /**
     * Serializable User
     * @property username Unique name of the user
     * @property name The personal name
     * @property permissionLevel User's permission level
     * @property credit Prepaid credit of the user
     * @property password Password of the user. Only available if the user has to be added to the system
     *
     */
    @Serializable
    data class PublicUser(
        val username: String,
        val name: String,
        val permissionLevel: PermissionLevel,
        val credit: Float,
        val password: String? = null
    )

    /**
     * User
     *
     * @property username Unique name of the user
     * @property name The personal name
     * @property permissionLevel User's permission level
     * @property credit Hashed Prepaid credit of the user
     * @property hash Hashed password of the user
     *
     */
    data class User(
        val username: String,
        val name: String,
        val permissionLevel: PermissionLevel,
        val credit: String,
        val hash: String
    )


    /**
     *  Request to the change the password of a user
     *  @property password Password of the caller
     *  @property username User where the new password will be updated. If it's the caller self, it can be null
     *  @property newPassword The new password for the requested user
     */
    @Serializable
    data class PasswordChangeRequest(
        val password: String,
        val username: String?,
        val newPassword: String
    )

    /**
     *
     *  Request to change the name of a user
     *
     * @property password Password of the caller (Admin)
     * @property username User where the new name will be set
     * @property newName The new name
     *
     */
    @Serializable
    data class NameChangeRequest(
        val password: String,
        val username: String,
        val newName: String
    )

    /**
     *
     *  Request to change the permission level of a user
     *
     * @property password Password of the caller (Admin)
     * @property username User where the new level will be set
     * @property newPermissionLevel The new permission level
     *
     */
    @Serializable
    data class PermissionChangeRequest(
        val password: String,
        val username: String,
        val newPermissionLevel: PermissionLevel
    )

    /**
     *
     *  Request to delete a user
     *
     * @property password Password of the caller (Admin)
     * @property username Username of the user
     *
     */
    @Serializable
    data class UserDeleteRequest(
        val password: String,
        val username: String
    )

    /**
     *
     *  Request to add a new user
     *
     * @property password Password of the caller (Admin)
     * @property user The new user which will be registered
     *
     */
    @Serializable
    data class UserAddRequest(
        val password: String,
        val user: PublicUser
    )


    /**
     * A Payment order from the past
     *
     * @property title Name of the order
     * @property price Price
     * @property creationTime Creation Time of the payment
     *
     */
    @Serializable
    data class Payment(
        val user: String,
        val title: String,
        val price: Float,
        @Serializable(with = InstantSerializer::class) val creationTime: Instant
    )

    enum class PermissionLevel(val int: Int) {
        ADMIN(2),
        WORKER(1),
        USER(0)
    }

    data class UserSession(
        val username: String,
    ): Principal




}

