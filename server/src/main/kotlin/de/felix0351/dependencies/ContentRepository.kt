package de.felix0351.dependencies

import de.felix0351.models.errors.DatabaseException.*
import de.felix0351.models.objects.Content
import org.litote.kmongo.Id
import kotlin.jvm.Throws


interface ContentRepository {

    //Categories
    @Throws(ValueAlreadyExistsException::class)
    suspend fun addCategory(category: Content.Category)

    suspend fun getCategories(): List<Content.Category>

    @Throws(NotFoundException::class)
    suspend fun deleteCategory(name: String)

    //Meal

    suspend fun addMeal(meal: Content.Meal)

    suspend fun getMeals(): List<Content.Meal>

    @Throws(NotFoundException::class)
    suspend fun updateMeal(meal: Content.Meal)

    @Throws(NotFoundException::class)
    suspend fun deleteMeal(id: Id<Content.Meal>)

    // Order

    suspend fun addOrder(order: Content.Order)

    suspend fun getOrders(): List<Content.Order>

    suspend fun getOrdersFromUser(username: String): List<Content.Order>

    @Throws(NotFoundException::class)
    suspend fun deleteOrder(id: Id<Content.Order>)

    // Report

    suspend fun addReport(report: Content.Report)

    suspend fun getReports(): List<Content.Report>

    @Throws(NotFoundException::class)
    suspend fun updateReport(report: Content.Report)

   @Throws(NotFoundException::class)
    suspend fun deleteReport(id: Id<Content.Report>)

}