package de.juliando.app.repository

import de.juliando.app.data.LocalDataStoreImpl
import de.juliando.app.data.ServerDataSourceImpl
import de.juliando.app.data.StorageKeys
import de.juliando.app.models.objects.Content
import de.juliando.app.models.objects.Result

class ContentRepositoryImpl(
    private val server: ServerDataSourceImpl = ServerDataSourceImpl(),
    private val cache: LocalDataStoreImpl = LocalDataStoreImpl()
) : ContentRepository {

    override suspend fun getMeals(): List<Content.Meal> {
        return try {
            val meals = server.getMeals()
            cache.storeList(meals, StorageKeys.MEAL.key)
            meals
        } catch (e: Exception) {
            cache.getList(StorageKeys.MEAL.key) ?: emptyList()
        }
    }

    override suspend fun getMeal(id: String): Content.Meal {
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

    override suspend fun getReports(): List<Content.Report> {
        TODO("Not yet implemented")
    }

    override suspend fun getReport(id: String): Content.Report {
        TODO("Not yet implemented")
    }

    override suspend fun newReport(report: Content.Report): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReport(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun editReport(report: Content.Report) {
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

    override suspend fun deleteSelection(selectionGroupName: String) {
        TODO("Not yet implemented")
    }
}