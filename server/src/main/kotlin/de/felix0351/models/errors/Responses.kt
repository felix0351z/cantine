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


//Konzept:

sealed class ServiceResult {

    //data class Success<T>(val result: T)
    //data class PermissionDenied()
    //data class DatabaseError(val message: String, val exception: InternalDatabaseException)


}