package de.felix0351.errors

class InternalDatabaseException: RuntimeException()


//Konzept:

sealed class ServiceResult {

    //data class Success<T>(val result: T)
    //data class PermissionDenied()
    //data class DatabaseError(val message: String, val exception: InternalDatabaseException)


}