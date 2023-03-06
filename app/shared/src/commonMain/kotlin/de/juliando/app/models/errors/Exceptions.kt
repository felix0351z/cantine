package de.juliando.app.models.errors

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

