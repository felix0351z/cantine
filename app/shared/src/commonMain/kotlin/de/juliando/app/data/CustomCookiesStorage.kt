package de.juliando.app.data

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*

class CustomCookiesStorage(
    private val localStorage: LocalDataStoreImpl = LocalDataStoreImpl()
) : CookiesStorage {
    /**
     * Stores the cookie in local storage
     */
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        localStorage.storeCookie(cookie)
    }

    /**
     * Gets the cookie from local storage
     */
    override suspend fun get(requestUrl: Url): List<Cookie> {
        val cookie: Cookie? = localStorage.getCookie()
        return if (cookie!=null) listOf(cookie)
               else              emptyList()
    }

    override fun close(){

    }
}