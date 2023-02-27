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

    inline fun <reified T> storeList(toStore: List<T>?, key: String) {
        if (toStore != null){
            settings[key] = Json.encodeToString(toStore)
        }else {
            settings.remove(key)
        }
    }

    inline fun <reified T> getList(key: String): List<T>? {
        var obj: String? = settings[key]
        return if (obj != null){
            Json.decodeFromString<List<T>>(obj)
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