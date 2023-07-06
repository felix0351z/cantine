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
    var BASE_URL: String = LocalDataStore.getURL()
) {

    /**
     * Login function to get cookie from Server with
     * @param username
     * @param password
     */
    suspend fun login(username: String, password: String) {
        BASE_URL = LocalDataStore.getURL()
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
        val response = httpClient.get("$BASE_URL/logout") {
            setAuthenticationCookie()
        }
        checkStatusCode(response)
        LocalDataStore.storeAuthenticationCookie(null)
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
        checkStatusCode(response)
        return response.body()
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
            if (id != null) setHeaderId(id)

            setAuthenticationCookie()
        }
        checkStatusCode(response)
        return response.body()
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
        checkStatusCode(response)
        return try {
            return response.body()
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
    suspend inline fun <reified T> delete(route: String, id: String? = null): T? {
        if(id!=null) {
            val response = httpClient.delete("$BASE_URL$route") {
                setHeaderId(id)
                setAuthenticationCookie()
            }
            checkStatusCode(response)

            return try {
                return response.body()
            } catch (e: Exception) {
                return null
            }
        }else{
            val response = httpClient.delete("$BASE_URL$route")
            checkStatusCode(response)
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
     * Function to load an image from the server
     *
     * @param route Route to the server
     * @return ByteArray of the picture
     */
    suspend fun loadImage(route: String) : ByteArray {
        val response = httpClient.get(route) {
            setAuthenticationCookie()
        }
        checkStatusCode(response)

        return response.readBytes()
    }

    /**
     * Checks the status code and throws an exception if the status code is not ok(200-299)
     *
     * @param response HttpResponse to check the status code
     */
    fun checkStatusCode(response: HttpResponse){
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
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