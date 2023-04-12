package de.felix0351.models.objects

import de.felix0351.utils.CustomIDSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id


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
    val user: Auth.PublicUser
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
    val newPermissionLevel: Auth.PermissionLevel
)

/**
 *  Request to add a new order
 *  @param meals List of all selected meals
 *
 */
@Serializable
data class CreateOrderRequest(
    val meals: List<CreateOrderRequestMeal>
)

/** Represents a selected meal
 * @property id id of the meal
 * @property selections Individual selections from the user
 */
@Serializable
data class CreateOrderRequestMeal(
    @Serializable(with = CustomIDSerializer::class) val id: Id<Content.Meal>,
    val selections: List<String>
)

/**
 * Request to verify a purchase at the mensa
 * @property username Name of the user
 * @property orderId Order of the user
 *
 */
@Serializable
data class VerifyOrderRequest(
    val username: String,
    @Serializable(with = CustomIDSerializer::class) val orderId: Id<Content.Order>
)

/**
 * Request to increase the amount of the user
 * @property password Password of the Worker/Admin
 * @property username User, which will get the new credit
 * @property credit Amount of the new credit
 */
@Serializable
data class AddCreditRequest(
    val password: String,
    val username: String,
    val credit: Float

)

