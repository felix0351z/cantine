package de.juliando.app.data

import de.juliando.app.models.errors.HttpStatusException.*
import de.juliando.app.models.objects.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ServerDataSourceImpl(

    private val httpClient: HttpClient = HttpClient {
        install(HttpCookies)
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    },
    private val BASE_URL: String = "https://..."

) : ServerDataSource {

    /**
     * Login function to get cookie from Server with
     * @param username
     * @param password
     */
    suspend fun HttpClient.login(username: String, password: String) = submitForm(
        url = "$BASE_URL/login",
        formParameters = Parameters.build {
            append("username", username)
            append("password", password)
        }
    )

    suspend fun HttpClient.logout() = get("$BASE_URL/logout")

    override suspend fun getMeals(): List<Content.Meal> {
        val response = httpClient.get("$BASE_URL/content/meals")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getAccount(): Auth.User {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(request: PasswordChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getCredit(): Float {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): List<Content.Order> {
        TODO("Not yet implemented")
    }

    override suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(id: String): Content.Order {
        TODO("Not yet implemented")
    }

    override suspend fun getPurchases(): List<Auth.Payment> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePurchases() {
        TODO("Not yet implemented")
    }

    override suspend fun getMeal(id: String): Content.Meal {
        TODO("Not yet implemented")
    }

    override suspend fun getReports(): List<Content.Report> {
        TODO("Not yet implemented")
    }

    override suspend fun getReport(id: String): Content.Report {
        TODO("Not yet implemented")
    }

    override suspend fun addUserCredit(request: AddCreditRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun verifyOrder(request: VerifyOrderRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun addMeal(newMeal: Content.Meal): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMeal(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun editMeal(meal: Content.Meal) {
        TODO("Not yet implemented")
    }

    override suspend fun newReport(report: Content.Report): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReport(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun editReport(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): List<Content.Category> {
        TODO("Not yet implemented")
    }

    override suspend fun newCategory(category: Content.Category): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(categoryName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getSelections(): List<Content.SelectionGroup> {
        TODO("Not yet implemented")
    }

    override suspend fun newSelection(selection: Content.SelectionGroup): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSelection(selectionName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): List<Auth.User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): List<Auth.User> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(request: UserAddRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(request: UserDeleteRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserName(request: NameChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserPassword(request: PasswordChangeRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserPermission(request: PermissionChangeRequest) {
        TODO("Not yet implemented")
    }

    /**
     * Checks which Status code
     *
     * @return exception for that status code
     */
    private fun checkStatusCode(statusCode: Int): Exception{
        TODO()
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