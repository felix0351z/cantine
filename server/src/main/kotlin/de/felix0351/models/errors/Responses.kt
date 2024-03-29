package de.felix0351.models.errors

import kotlinx.serialization.Serializable


/**
 * A ServiceError contains an id and a description.
 *
 * The error will be serialized and responded to the caller
 * @see ErrorCode
 */
@Serializable
data class RouteError(
    val id: Int,
    val description: String
)


/**
 * Describes all possible errors which can occur
 * @property NoPermission Will be returned if the logged-in user tries to call a route which needs more permissions than he have (User/Worker/Admin)
 * @property AlreadyExists Will be returned if the user tries to insert something unique which already exists (example: user or category)
 * @property NotFound Will be returned if the requested value won't be found
 * @property SameValue Will be returned if the user tries to set something with the same value (password or permission-level)
 *
 * @see DatabaseException
 */
enum class ErrorCode(val code: Int) {

    InternalDatabaseError(0),
    ContentTransformationError(1),
    IllegalID(2),
    NoPermission(3),
    AlreadyExists(4),
    NotFound(5),
    SameValue(6),
    WrongPassword(7),
    NoPassword(8),
    Unauthorized(9),
    NotEnoughMoney(10),
    FileIOException(11)

}