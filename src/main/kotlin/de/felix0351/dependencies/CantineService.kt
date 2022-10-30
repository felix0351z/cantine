package de.felix0351.dependencies

class CantineService(
    private val authRepository: AuthenticationRepository,
    private val contentRepository: ContentRepository
    ) {


    suspend fun checkUserCredentials(username: String, password: String): Boolean {
        TODO("Implement")
    }









}