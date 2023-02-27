package de.juliando.app.data

import de.juliando.app.models.objects.Auth
import de.juliando.app.models.objects.Content
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