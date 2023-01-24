package de.felix0351.models.errors

sealed class DatabaseException: RuntimeException() {
    class InternalException: DatabaseException()
    class NotFoundException: DatabaseException()
    class SameValueException: DatabaseException()
    class ValueAlreadyExistsException: DatabaseException()

}

class IllegalIdException: RuntimeException()
class NoPasswordException: RuntimeException()
class WrongPasswordException: RuntimeException()

class IncorrectOrderException: RuntimeException()

class NotEnoughMoneyException(val minus: Float): RuntimeException()