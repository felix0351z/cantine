package de.juliando.app.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import de.juliando.app.models.objects.Auth
import de.juliando.app.models.objects.Content
import de.juliando.app.utils.CookieSerializable
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalDataStoreImpl : LocalDataStore {
    private val settings: Settings by lazy { Settings() }

    override fun storeMeals(meals: List<Content.Meal>?) {
        if (meals != null){
            settings[StorageKeys.MEAL.key] = Json.encodeToString(meals)
        }else {
            settings.remove(StorageKeys.MEAL.key)
        }
    }
    override fun getMeals(): List<Content.Meal>? {
        var meals: String? = settings[StorageKeys.MEAL.key]
        return if (meals != null){
            Json.decodeFromString<List<Content.Meal>>(meals)
        }else {
            null
        }
    }

    override fun storeReports(reports: List<Content.Report>?) {
        if (reports != null){
            settings[StorageKeys.REPORT.key] = Json.encodeToString(reports)
        }else {
            settings.remove(StorageKeys.REPORT.key)
        }
    }
    override fun getReports(): List<Content.Report>? {
        var reports: String? = settings[StorageKeys.REPORT.key]
        return if (reports != null){
            Json.decodeFromString<List<Content.Report>>(reports)
        }else {
            null
        }
    }

    override fun storeCategories(categories: List<Content.Category>?) {
        if (categories != null){
            settings[StorageKeys.CATEGORY.key] = Json.encodeToString(categories)
        }else {
            settings.remove(StorageKeys.CATEGORY.key)
        }
    }
    override fun getCategories(): List<Content.Category>? {
        var categories: String? = settings[StorageKeys.CATEGORY.key]
        return if (categories != null){
            Json.decodeFromString<List<Content.Category>>(categories)
        }else {
            null
        }
    }

    override fun storeSelections(selections: List<Content.Selection>?) {
        if (selections != null){
            settings[StorageKeys.SELECTION.key] = Json.encodeToString(selections)
        }else {
            settings.remove(StorageKeys.SELECTION.key)
        }
    }
    override fun getSelections(): List<Content.Selection>? {
        var selections: String? = settings[StorageKeys.SELECTION.key]
        return if (selections != null){
            Json.decodeFromString<List<Content.Selection>>(selections)
        }else {
            null
        }
    }

    override fun storeOrders(orders: List<Content.Order>?) {
        if (orders != null){
            settings[StorageKeys.ORDER.key] = Json.encodeToString(orders)
        }else {
            settings.remove(StorageKeys.ORDER.key)
        }
    }
    override fun getOrders(): List<Content.Order>? {
        var orders: String? = settings[StorageKeys.ORDER.key]
        return if (orders != null){
            Json.decodeFromString<List<Content.Order>>(orders)
        }else {
            null
        }
    }

    override fun storePurchases(purchases: List<Auth.Payment>?) {
        if (purchases != null){
            settings[StorageKeys.PAYMENT.key] = Json.encodeToString(purchases)
        }else {
            settings.remove(StorageKeys.PAYMENT.key)
        }
    }
    override fun getPurchases(): List<Auth.Payment>? {
        var purchases: String? = settings[StorageKeys.PAYMENT.key]
        return if (purchases != null){
            Json.decodeFromString<List<Auth.Payment>>(purchases)
        }else {
            null
        }
    }

    /**
     * Functions to store and get a Cookie
     */
    override fun storeCookie(cookie: Cookie?) {
        if (cookie != null){
            settings[StorageKeys.COOKIE.key] = CookieSerializable.serializeCookie(cookie)
        }else {
            settings.remove(StorageKeys.COOKIE.key)
        }
    }
    override fun getCookie(): Cookie? {
        var cookie: String? = settings[StorageKeys.PAYMENT.key]
        return if (cookie != null){
            CookieSerializable.deserializeCookie(cookie)
        }else {
            null
        }
    }


}