package de.juliando.app.data

import de.juliando.app.models.errors.HttpStatusException.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ServerDataSourceImpl(

    val httpClient: HttpClient = HttpClient {
        install(HttpCookies) {
            storage = CustomCookiesStorage()
        }
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    },
    //Test Server
    val BASE_URL: String = "https://185.215.180.245"

) : ServerDataSource {

    /**
     * Login function to get cookie from Server with
     * @param username
     * @param password
     */
    private suspend fun HttpClient.login(username: String, password: String) = submitForm(
        url = "$BASE_URL/login",
        formParameters = Parameters.build {
            append("username", username)
            append("password", password)
        }
    ){
        https()
    }
    suspend fun login(username: String, password: String){
        httpClient.login(username, password)
    }

    /**
     * Logout from current session.
     */
    private suspend fun HttpClient.logout() = get("$BASE_URL/logout"){
        https()
    }
    suspend fun logout(){
        httpClient.logout()
    }

    private fun HttpRequestBuilder.https() {
        url {
            protocol = URLProtocol.HTTPS
        }
    }

    /**
     * Generic function to get a List from the server.
     *
     * @param route Route to the server
     * @return if successful returns the list from the server
     *
     */
    suspend inline fun <reified T> getList(route: String): List<T> {
        val response = httpClient.get("$BASE_URL$route")
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
            contentType(ContentType.Application.Json)
            setBody(id)
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
    suspend inline fun <reified T,reified R> post(route: String, request: T): R?{
        val response = httpClient.post("$BASE_URL$route") {
            contentType(ContentType.Application.Json)
            setBody(request)
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
                contentType(ContentType.Application.Json)
                setBody(id)
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
    suspend inline fun <reified T> put(route: String, toPut: T){
        val response: HttpResponse = httpClient.put("$BASE_URL$route") {
            contentType(ContentType.Application.Json)
            setBody(toPut)
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