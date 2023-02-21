package de.juliando.app.data

import de.juliando.app.models.objects.Auth
import de.juliando.app.models.objects.Content

interface LocalDataStore {

    fun storeMeals(meals: List<Content.Meal>?)
    fun getMeals(): List<Content.Meal>?

    fun storeReports(reports: List<Content.Report>?)
    fun getReports(): List<Content.Report>?

    fun storeCategories(categories: List<Content.Category>?)
    fun getCategories(): List<Content.Category>?

    fun storeSelections(selections: List<Content.Selection>?)
    fun getSelections(): List<Content.Selection>?

    fun storeOrders(orders: List<Content.Order>?)
    fun getOrders(): List<Content.Order>?

    fun storePurchases(selections: List<Auth.Payment>?)
    fun getPurchases(): List<Auth.Payment>?
}