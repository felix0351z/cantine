package de.juliando.app.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


//TODO: Save the URL as Key/Value pair -> For Login Screen
//TODO: Function to save the current user as an value -> For Login Screen

object LocalDataStore {
    val settings: Settings by lazy { Settings() }
    val url = "https://185.215.180.245"

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
        var obj: String? = settings[key]
        return if (obj != null){
            Json.decodeFromString<List<T>>(obj)
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