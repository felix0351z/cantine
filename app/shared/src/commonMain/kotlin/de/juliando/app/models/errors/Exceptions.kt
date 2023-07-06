package de.juliando.app.models.errors

/**
 * Exceptions coming from the server.
 */
sealed class HttpStatusException: RuntimeException() {
    class InternalDatabaseErrorException : HttpStatusException()
    class ContentTransformationErrorException : HttpStatusException()
    class IllegalIdException : HttpStatusException()
    class NoPermissionException : HttpStatusException()
    class AlreadyExistsException : HttpStatusException()
    class NotFoundException : HttpStatusException()
    class SameValueException : HttpStatusException()
    class WrongPasswordException : HttpStatusException()
    class NoPasswordException : HttpStatusException()
    class UnauthorizedException : HttpStatusException()
    class NotEnoughMoneyException : HttpStatusException()
}

/**
 * Will be thrown if an request was executed, even if
 * there is no active session for a user
 */
class NoSessionException(msg: String) : RuntimeException(msg)

