package de.juliando.app.repository

import de.juliando.app.models.objects.*

interface ContentRepository {

    //Meal
    @Throws(Exception::class)
    suspend fun getMeals(): List<Content.Meal>
    @Throws(Exception::class)
    suspend fun getMeal(id: String): Content.Meal
    @Throws(Exception::class)
    suspend fun addMeal(newMeal: Content.Meal): String
    @Throws(Exception::class)
    suspend fun deleteMeal(id: String)
    @Throws(Exception::class)
    suspend fun editMeal(meal: Content.Meal)

    //Report
    @Throws(Exception::class)
    suspend fun getReports(): List<Content.Report>
    @Throws(Exception::class)
    suspend fun getReport(id: String): Content.Report
    @Throws(Exception::class)
    suspend fun newReport(report: Content.Report): String
    @Throws(Exception::class)
    suspend fun deleteReport(id: String)
    @Throws(Exception::class)
    suspend fun editReport(report: Content.Report)
    @Throws(Exception::class)

    //Category
    suspend fun getCategories(): List<Content.Category>
    @Throws(Exception::class)
    suspend fun newCategory(category: Content.Category): String
    @Throws(Exception::class)
    suspend fun deleteCategory(categoryName: String)
    @Throws(Exception::class)

    //Selection
    suspend fun getSelections(): List<Content.SelectionGroup>
    @Throws(Exception::class)
    suspend fun newSelection(selection: Content.SelectionGroup): String
    @Throws(Exception::class)
    suspend fun deleteSelection(selectionGroupName: String)

}