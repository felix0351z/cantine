package de.juliando.app.data

import de.juliando.app.models.objects.*
import io.ktor.client.*

interface ServerDataSource {

    //No permission
    @Throws(Exception::class)
    suspend fun getMeals(): List<Content.Meal>
    @Throws(Exception::class)
    suspend fun getAccount(): Auth.User
    @Throws(Exception::class)
    suspend fun changePassword(request: PasswordChangeRequest)
    @Throws(Exception::class)
    suspend fun getCredit(): Float
    @Throws(Exception::class)
    suspend fun getOrders(): List<Content.Order>
    @Throws(Exception::class)
    suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order
    @Throws(Exception::class)
    suspend fun cancelOrder(id: String): Content.Order
    @Throws(Exception::class)
    suspend fun getPurchases(): List<Auth.Payment>
    @Throws(Exception::class)
    suspend fun deletePurchases()
    @Throws(Exception::class)
    suspend fun getMeal(id: String): Content.Meal
    @Throws(Exception::class)
    suspend fun getReports(): List<Content.Report>
    @Throws(Exception::class)
    suspend fun getReport(id: String): Content.Report


    //Permission worker
    @Throws(Exception::class)
    suspend fun addUserCredit(request: AddCreditRequest)
    @Throws(Exception::class)
    suspend fun verifyOrder(request: VerifyOrderRequest)
    @Throws(Exception::class)
    suspend fun addMeal(newMeal: Content.Meal): String
    @Throws(Exception::class)
    suspend fun deleteMeal(id: String)
    @Throws(Exception::class)
    suspend fun editMeal(meal: Content.Meal)
    @Throws(Exception::class)
    suspend fun newReport(report: Content.Report): String
    @Throws(Exception::class)
    suspend fun deleteReport(id: String)
    @Throws(Exception::class)
    suspend fun editReport(report: Content.Report)
    @Throws(Exception::class)
    suspend fun getCategories(): List<Content.Category>
    @Throws(Exception::class)
    suspend fun newCategory(category: Content.Category): String
    @Throws(Exception::class)
    suspend fun deleteCategory(categoryName: String)
    @Throws(Exception::class)
    suspend fun getSelections(): List<Content.SelectionGroup>
    @Throws(Exception::class)
    suspend fun newSelection(selection: Content.SelectionGroup): String
    @Throws(Exception::class)
    suspend fun deleteSelection(selectionGroupName: String)

    //Permission admin
    @Throws(Exception::class)
    suspend fun getUsers(): List<Auth.User>
    @Throws(Exception::class)
    suspend fun getUser(username: String): List<Auth.User>
    @Throws(Exception::class)
    suspend fun addUser(request: UserAddRequest)
    @Throws(Exception::class)
    suspend fun deleteUser(request: UserDeleteRequest)
    @Throws(Exception::class)
    suspend fun changeUserName(request: NameChangeRequest)
    @Throws(Exception::class)
    suspend fun setUserPassword(request: PasswordChangeRequest)
    @Throws(Exception::class)
    suspend fun changeUserPermission(request: PermissionChangeRequest)
}