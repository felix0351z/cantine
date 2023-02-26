package de.juliando.app.data

import de.juliando.app.models.errors.HttpStatusException.*
import de.juliando.app.models.objects.*
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

    private val httpClient: HttpClient = HttpClient {
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
    private val BASE_URL: String = "https://185.215.180.245"

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
    ){
        https()
    }

    suspend fun HttpClient.logout() = get("$BASE_URL/logout"){
        https()
    }

    private fun HttpRequestBuilder.https() {
        url {
            protocol = URLProtocol.HTTPS
        }
    }

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
        val response = httpClient.get("$BASE_URL/account")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun changePassword(request: PasswordChangeRequest) {
        val response = httpClient.post("$BASE_URL/account/password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getCredit(): Float {
        val response = httpClient.get("$BASE_URL/payment/credit")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getOrders(): List<Content.Order> {
        val response = httpClient.get("$BASE_URL/payment/orders")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order {
        val response = httpClient.post("$BASE_URL/payment/order") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun cancelOrder(id: String): Content.Order {
        val response = httpClient.delete("$BASE_URL/payment/order") {
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

    override suspend fun getPurchases(): List<Auth.Payment> {
        val response = httpClient.get("$BASE_URL/payment/purchases")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deletePurchases() {
        val response = httpClient.delete("$BASE_URL/payment/purchases")
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getMeal(id: String): Content.Meal {
        val response = httpClient.get("$BASE_URL/content/meal"){
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

    override suspend fun getReports(): List<Content.Report> {
        val response = httpClient.get("$BASE_URL/content/reports")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getReport(id: String): Content.Report {
        val response = httpClient.get("$BASE_URL/content/report"){
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

    override suspend fun addUserCredit(request: AddCreditRequest) {
        val response: HttpResponse = httpClient.post("$BASE_URL/payment/credit") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun verifyOrder(request: VerifyOrderRequest) {
        val response: HttpResponse = httpClient.post("$BASE_URL/payment/purchase") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun addMeal(newMeal: Content.Meal): String {
        val response: HttpResponse = httpClient.post("$BASE_URL/content/meal") {
            contentType(ContentType.Application.Json)
            setBody(newMeal)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else {
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deleteMeal(id: String) {
        val response = httpClient.delete("$BASE_URL/content/meal"){
            contentType(ContentType.Application.Json)
            setBody(id)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun editMeal(meal: Content.Meal) {
        val response: HttpResponse = httpClient.put("$BASE_URL/content/meal") {
            contentType(ContentType.Application.Json)
            setBody(meal)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun newReport(report: Content.Report): String {
        val response: HttpResponse = httpClient.post("$BASE_URL/content/report") {
            contentType(ContentType.Application.Json)
            setBody(report)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else {
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deleteReport(id: String) {
        val response = httpClient.delete("$BASE_URL/content/report"){
            contentType(ContentType.Application.Json)
            setBody(id)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun editReport(report: Content.Report) {
        val response: HttpResponse = httpClient.put("$BASE_URL/content/report") {
            contentType(ContentType.Application.Json)
            setBody(report)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getCategories(): List<Content.Category> {
        val response = httpClient.get("$BASE_URL/content/categories")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun newCategory(category: Content.Category): String {
        val response: HttpResponse = httpClient.post("$BASE_URL/content/category") {
            contentType(ContentType.Application.Json)
            setBody(category)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else {
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deleteCategory(categoryName: String) {
        val response = httpClient.delete("$BASE_URL/content/category"){
            contentType(ContentType.Application.Json)
            setBody(categoryName)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getSelections(): List<Content.SelectionGroup> {
        val response = httpClient.get("$BASE_URL/content/selections")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun newSelection(selection: Content.SelectionGroup): String {
        val response: HttpResponse = httpClient.post("$BASE_URL/content/selection") {
            contentType(ContentType.Application.Json)
            setBody(selection)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else {
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deleteSelection(selectionGroupName: String) {
        val response = httpClient.delete("$BASE_URL/content/selection"){
            contentType(ContentType.Application.Json)
            setBody(selectionGroupName)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getUsers(): List<Auth.User> {
        val response = httpClient.get("$BASE_URL/users")
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun getUser(username: String): List<Auth.User> {
        val response = httpClient.get("$BASE_URL/user"){
            contentType(ContentType.Application.Json)
            setBody(username)
        }
        val httpStatus = response.status.value
        return if (httpStatus in 200..299){
            response.body()
        }else{
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun addUser(request: UserAddRequest) {
        val response = httpClient.post("$BASE_URL/user") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun deleteUser(request: UserDeleteRequest) {
        val response = httpClient.delete("$BASE_URL/user"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun changeUserName(request: NameChangeRequest) {
        val response = httpClient.post("$BASE_URL/user/name") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun setUserPassword(request: PasswordChangeRequest) {
        val response = httpClient.post("$BASE_URL/user/password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val httpStatus = response.status.value
        if (httpStatus !in 200..299){
            throw checkStatusCode(httpStatus)
        }
    }

    override suspend fun changeUserPermission(request: PermissionChangeRequest) {
        val response = httpClient.post("$BASE_URL/user/permission") {
            contentType(ContentType.Application.Json)
            setBody(request)
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
    private fun checkStatusCode(statusCode: Int): Exception{
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