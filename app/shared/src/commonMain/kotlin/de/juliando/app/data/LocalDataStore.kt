package de.juliando.app.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import de.juliando.app.models.objects.backend.Auth
import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.utils.asDisplayable
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


//TODO: Save the URL as Key/Value pair -> For Login Screen
//TODO: Function to save the current user as an value -> For Login Screen

object LocalDataStore {
    val settings: Settings by lazy { Settings() }
    //val url = "http://207.180.215.119:8080/api"


    /**
     *Function to store the URL.
     *
     * @param url URL(String) to store in local storage
     */
    inline fun storeURL(url: String?) {
        storeString(url, StorageKeys.URL.key)
    }

    /**
     *Function to get the URL.
     *
     * @return URL from the local storage
     */
    inline fun getURL(): String {
        return getString(StorageKeys.URL.key) ?: return ""
    }

    /**
     *Function to store the current user.
     *
     * @param user to store in local storage
     */
    inline fun storeCurrentUser(user: Auth.User?) {
        if (user != null){
            settings[StorageKeys.USER.key] = Json.encodeToString(user)
        }else {
            settings.remove(StorageKeys.USER.key)
        }
    }

    /**
     *Function to get the current user.
     *
     * @return user from the local storage
     */
    inline fun getCurrentUser(): Auth.User? {
        val obj: String? = settings[StorageKeys.USER.key]
        return if (obj != null){
            Json.decodeFromString(obj)
        }else {
            null
        }
    }

    /**
     *Generic function to store a List with a key.
     *
     * @param toStore List to store in local storage
     * @param key Key to access the storage
     */
    inline fun <reified T> storeList(toStore: List<T>?, key: String) {
        if (toStore != null){
            settings[key] = Json.encodeToString(toStore)
        }else {
            settings.remove(key)
        }
    }

    /**
     *Generic function to get a List with a key.
     *
     * @param key Key to access the storage
     * @return List from the local storage
     */
    inline fun <reified T> getList(key: String): List<T>? {
        val obj: String? = settings[key]
        return if (obj != null){
            Json.decodeFromString<List<T>>(obj)
        }else {
            null
        }
    }

    /**
     *  Function to get the Meal with the [id] from the stored List.
     *
     * @param id of the Meal
     * @return Meal or null
     */
    inline fun getMealFromList(id: String): Content.Meal? {
        val listString: String? = settings[StorageKeys.MEAL.key]
        return if (listString != null){
            val list = Json.decodeFromString<List<Content.Meal>>(listString)
            list.find { it.id.equals(id) }
        } else {
            null
        }
    }

    /**
     *  Function to get the Report with the [id] from the stored List.
     *
     * @param id of the Report
     * @return Report or null
     */
    inline fun getReportFromList(id: String): Content.Report? {
        val listString: String? = settings[StorageKeys.REPORT.key]
        return if (listString != null){
            val list = Json.decodeFromString<List<Content.Report>>(listString)
            list.find { it.id.equals(id) }
        } else {
            null
        }
    }

    /**
     *  Function to get the Order with the [id] from the stored List.
     *
     * @param id of the Report
     * @return Order or null
     */
    inline fun getOrderFromList(id: String): Order? {
        val listString: String? = settings[StorageKeys.ORDER.key]
        return if (listString != null) {
            val list = Json.decodeFromString<List<Content.Order>>(listString)
            list.find { it.id == id }?.asDisplayable()
        } else {
            null
        }
    }

    /**
     *Generic function to store a String with a key.
     *
     * @param toStore String to store in local storage
     * @param key Key to access the storage
     */
     fun storeString(toStore: String?, key: String) {
        if (toStore != null){
            settings[key] = toStore
        }else {
            settings.remove(key)
        }
     }

    /**
     *Generic function to get a String with a key.
     *
     * @param key Key to access the storage
     * @return String from the local storage
     */
    fun getString(key: String): String? {
        return settings[key]
    }

    /**
     *Generic function to store a Object with a key.
     *
     * @param toStore Object to store in local storage
     * @param key Key to access the storage
     */
    inline fun <reified T> storeObject(toStore: T?, key: String) {
        if (toStore != null){
            settings[key] = Json.encodeToString(toStore)
        }else {
            settings.remove(key)
        }
    }

    /**
     *Generic function to get a Object with a key.
     *
     * @param key Key to access the storage
     * @return Object from the local storage
     */
    inline fun <reified T> getObject(key: String): T? {
        val obj: String? = settings[key]
        return if (obj != null){
            Json.decodeFromString<T>(obj)
        }else {
            null
        }
    }

    /**
     * Function to and remove the authentication cookie
     * @param cookie Cookie to store, If null is provided, the current cookie will be removed
     */
    fun storeAuthenticationCookie(cookie: Cookie?) {
        if (cookie != null){
            settings[StorageKeys.COOKIE.key] = renderCookieHeader(cookie)
        }else {
            settings.remove(StorageKeys.COOKIE.key)
        }
    }

    /**
     * Get the current authentication cookie
     * @return Authentication cookie as string header value.
     * Will be null if no cookie is available
     */
    fun getAuthenticationCookieHeader(): String? {
        return settings[StorageKeys.COOKIE.key]
    }


}