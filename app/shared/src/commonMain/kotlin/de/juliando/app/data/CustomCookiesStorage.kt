package de.juliando.app.data

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate

class CustomCookiesStorage(
    private val localStorage: LocalDataStoreImpl = LocalDataStoreImpl()
) : CookiesStorage {
    /**
     * Sets a [cookie] for the specified host.
     */
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        localStorage.storeCookie(cookie)
    }

    /**
     * Gets a map of [String] to [Cookie] for a specific host.
     */
    override suspend fun get(requestUrl: Url): List<Cookie> {
        val cookie: Cookie? = localStorage.getCookie()
        return if (cookie!=null) listOf(cookie)
               else              emptyList()
    }

    override fun close(){

    }
}