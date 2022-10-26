package de.felix0351.plugins

import de.felix0351.db.AuthenticationRepository
import de.felix0351.models.objects.UserSession
import de.felix0351.utils.FileHandler

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

import kotlin.collections.set
import kotlin.time.Duration.Companion.days


const val AUTHENTICATED_PATH = "/user" //Path which needs a session

fun Application.configureSecurity() {


    install(Sessions) {
        val sessionAge = FileHandler.configuration.authentication.session_age
        val signKey = hex(FileHandler.configuration.authentication.signKey)


        cookie<UserSession>("user_session", AuthenticationRepository.AuthenticationSessionStorage()) {

            // Only transfer cookies via ssl encrypted connection
            cookie.secure = true

            // Permanent session has a max age
            cookie.maxAge = sessionAge.days

            // Prevent cross-site request forgery attacks
            cookie.extensions["SameSite"] = "strict"

            // Path to the content, which needs a session
            cookie.path = AUTHENTICATED_PATH

            // Prevents user to edit the cookie, but can show the content
            transform(SessionTransportTransformerMessageAuthentication(signKey))
        }
    }

}

