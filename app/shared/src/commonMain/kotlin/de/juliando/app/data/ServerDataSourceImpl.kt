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
     * Logout from current session
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

    suspend inline fun <reified T> getList(route: String): List<T> {
        val response = httpClient.get("$BASE_URL$route")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    suspend inline fun <reified T> getObject(route: String, id: String? = null): T {
        val response = httpClient.get("$BASE_URL$route"){
            contentType(ContentType.Application.Json)
            setBody(id)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    suspend inline fun <reified T,reified R> post(route: String, request: T): R?{
        val response = httpClient.post("$BASE_URL$route") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        return try {
            if (httpStatus in 200..299) response.body()
            else                              throw checkStatusCode(httpStatus)
        }catch (e: Exception){
            return null
        }
    }

    suspend inline fun <reified T, reified R> delete(route: String, id: T? = null): R? {
        if(id!=null) {
            val response = httpClient.delete("$BASE_URL$route") {
                contentType(ContentType.Application.Json)
                setBody(id)
            }
            val httpStatus = response.status.value
            return try {
                if (httpStatus in 200..299) response.body()
                else throw checkStatusCode(httpStatus)
            } catch (e: Exception) {
                return null
            }
        }else{
            httpClient.delete("$BASE_URL$route")
            return null
        }
    }

    suspend inline fun <reified T> put(route: String, toPut: T){
        val response: HttpResponse = httpClient.put("$BASE_URL$route") {
            contentType(ContentType.Application.Json)
            setBody(toPut)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    /**
     * Checks which Status code
     *
     * @return exception for that status code
     */
    fun checkStatusCode(statusCode: Int): Exception{
        return when(statusCode){
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