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
    val settings: Settings by lazy { Settings() }

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
     * Functions to store and get a Cookie.
     *
     * @param cookie Cookie to store
     * Serializes the cookie with the custom cookie serializer.
     * If parameter is null, remove cookie from storage
     *
     */
    override fun storeCookie(cookie: Cookie?) {
        if (cookie != null){
            settings[StorageKeys.COOKIE.key] = CookieSerializable.serializeCookie(cookie)
        }else {
            settings.remove(StorageKeys.COOKIE.key)
        }
    }
    override fun getCookie(): Cookie? {
        var cookie: String? = settings[StorageKeys.COOKIE.key]
        return if (cookie != null){
            CookieSerializable.deserializeCookie(cookie)
        }else {
            null
        }
    }


}