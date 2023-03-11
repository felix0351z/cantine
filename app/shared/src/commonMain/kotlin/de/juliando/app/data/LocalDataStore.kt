package de.juliando.app.data

import io.ktor.http.*

/**
 * Stores data as cache
 */

interface LocalDataStore {

    /**
     * Functions to store and get a Cookie
     */
    fun storeCookie(cookie: Cookie?)
    fun getCookie(): Cookie?
}