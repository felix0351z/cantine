package de.juliando.app.repository

import de.juliando.app.data.LocalDataStore
import de.juliando.app.data.ServerDataSource
import de.juliando.app.data.StorageKeys
import de.juliando.app.models.objects.Content

/**
 * This repository handles the content data.
 * It decides whether the data comes from the server or from local storage.
 */

class ContentRepositoryImpl(
    private val server: ServerDataSource = ServerDataSource(),
) : ContentRepository {

    override suspend fun getMeals(): List<Content.Meal> {
        return try {
            // Try to get the Data from the Server
            val meals = server.getList<Content.Meal>("/content/meals")
            LocalDataStore.storeList(meals, StorageKeys.MEAL.key)
            meals
        } catch (e: Exception) {
            // Catch: get the Data from the local Storage. If nothing is stored return an empty list.
            LocalDataStore.getList(StorageKeys.MEAL.key) ?: emptyList()
        }
    }


    override suspend fun loadPicture(model: String): ByteArray {
        return server.loadImage(model)
    }

    override suspend fun getMeal(id: String): Content.Meal {
        return server.get("/content/meal", id)
    }

    override suspend fun addMeal(newMeal: Content.Meal): String? {
        return server.post("/content/meal", newMeal)
    }

    override suspend fun deleteMeal(id: String): Content.Meal? {
        return server.delete("/content/meal", id)
    }

    override suspend fun editMeal(meal: Content.Meal) {
        server.put("/content/meal", meal)
    }

    override suspend fun getReports(): List<Content.Report> {
        return try {
            // Try to get the Data from the Server
            val reports: List<Content.Report> = server.get("/content/reports")
            LocalDataStore.storeList(reports, StorageKeys.REPORT.key)
            reports
        } catch (e: Exception) {
            e.printStackTrace()
            // Catch: get the Data from the local Storage. If nothing is stored return an empty list.
            LocalDataStore.getList(StorageKeys.REPORT.key) ?: emptyList()
        }
    }

    override suspend fun getReport(id: String): Content.Report {
        return server.get("/content/report", id)
    }

    override suspend fun newReport(report: Content.Report): String? {
        return server.post("/content/report", report)
    }

    override suspend fun deleteReport(id: String) {
       server.delete<String, String>("/content/report", id)
    }

    override suspend fun editReport(report: Content.Report) {
        server.put("/content/report", report)
    }

    override suspend fun getCategories(): List<Content.Category> {
        return server.getList("/content/categories")
    }

    override suspend fun newCategory(category: Content.Category): String? {
        return server.post("/content/category", category)
    }

    override suspend fun deleteCategory(categoryName: String) {
        server.delete<String, String>("/content/category", categoryName)
    }

    override suspend fun getSelections(): List<Content.SelectionGroup> {
        return server.getList("/content/selections")
    }

    override suspend fun newSelection(selection: Content.SelectionGroup): String? {
        return server.post("/content/selection", selection)
    }

    override suspend fun deleteSelection(selectionGroupName: String) {
        server.delete<String, String>("/content/selection", selectionGroupName)
    }
}