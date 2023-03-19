package de.juliando.app.data

import de.juliando.app.models.errors.HttpStatusException.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

//TODO: Correct Exception handling for the front-end in the server source and all repositories
class ServerDataSource(

    val httpClient: HttpClient = createHttpClient(),
    val BASE_URL: String = LocalDataStore.url
) {

    /**
     * Login function to get cookie from Server with
     * @param username
     * @param password
     */
    suspend fun login(username: String, password: String) {
        val response = httpClient.submitForm(
            url = "$BASE_URL/login",
            formParameters = Parameters.build {
                append("username", username)
                append("password", password)
            }
        )
        checkStatusCode(response) // If there is a not acceptable status code, it will be thrown

        val cookie = response.setCookie()[0]
        LocalDataStore.storeAuthenticationCookie(cookie)
    }
    /**
     * Logout from current session.
     */
    suspend fun logout() {
        val response = httpClient.get("$BASE_URL/logout") { setAuthenticationCookie() }
        checkStatusCode(response)
    }

    /**
     * Generic function to get a List from the server.
     *
     * @param route Route to the server
     * @return if successful returns the list from the server
     *
     */
    suspend inline fun <reified T> getList(route: String): List<T> {
        val response = httpClient.get("$BASE_URL$route") {
            setAuthenticationCookie()
        }
        return if(checkStatusCode(response)) response.body()
        else                                 throw Exception()
    }

    /**
     * Generic function to get a object from the server.
     *
     * @param route Route to the server
     * @param id Id from the requested object(can be null)
     * @return if successful returns the object from the server
     *
     */
    suspend inline fun <reified T> get(route: String, id: String? = null): T {
        val response = httpClient.get("$BASE_URL$route"){
            if (id != null) setJsonBody(id)
            setAuthenticationCookie()
        }
        return if(checkStatusCode(response)) response.body()
        else                                 throw Exception()
    }

    /**
     * Generic function to send a request to the server.
     *
     * @param route Route to the server
     * @param request Request to send to the server
     * @return returns the response from the server(can be null)
     *
     */
    suspend inline fun <reified T : Any, reified R> post(route: String, request: T): R?{
        val response = httpClient.post("$BASE_URL$route") {
            setJsonBody(request)
            setAuthenticationCookie()
        }
        return try {
            return if(checkStatusCode(response)) response.body()
            else                                 throw Exception()
        }catch (e: Exception){
            return null
        }
    }

    /**
     * Generic function to delete a object from the server.
     *
     * @param route Route to the server
     * @param id Id from the requested object(can be null)
     * @return returns the response from the server(can be null)
     *
     */
    suspend inline fun <reified T, reified R> delete(route: String, id: T? = null): R? {
        if(id!=null) {
            val response = httpClient.delete("$BASE_URL$route") {
                setJsonBody(id)
                setAuthenticationCookie()
            }
            return try {
                return if(checkStatusCode(response)) response.body()
                else                                 throw Exception()
            } catch (e: Exception) {
                return null
            }
        }else{
            httpClient.delete("$BASE_URL$route")
            return null
        }
    }

    /**
     * Generic function to change a object from the server.
     *
     * @param route Route to the server
     * @param toPut The changed object
     * @return returns the response from the server(can be null)
     *
     */
    suspend inline fun <reified T: Any> put(route: String, toPut: T){
        val response: HttpResponse = httpClient.put("$BASE_URL$route") {
            setJsonBody(toPut)
            setAuthenticationCookie()
        }
        checkStatusCode(response)
    }

    /**
     * Checks which Status code
     *
     * @param response HttpResponse to check
     * @return true if status code is ok else throws an Error
     */
    fun checkStatusCode(response: HttpResponse): Boolean{
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            true
        }else{
            throw when(httpStatus){
                500 -> InternalDatabaseErrorException()
                400 -> ContentTransformationErrorException()
                403 -> NoPermissionException()
                409 -> AlreadyExistsException()
                404 -> NotFoundException()
                401 -> UnauthorizedException()
                else -> {RuntimeException()}
            }
        }
    }

}