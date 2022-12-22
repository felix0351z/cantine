package de.felix0351.models.errors

sealed class Response {

    object Ok : Response()
    data class Error(val code: ErrorCode): Response()

}

enum class ErrorCode(val code: Int) {

    AlreadyExists(1),
    NotFound(2),
    SameValue(3)

}