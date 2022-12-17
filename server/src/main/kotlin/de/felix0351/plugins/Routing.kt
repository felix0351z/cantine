package de.felix0351.plugins

import de.felix0351.routes.authenticationRoutes
import de.felix0351.routes.contentRoutes

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    install(StatusPages) {
        status(HttpStatusCode.Forbidden) { call, _ ->
            call.respond("403: No valid session")
        }
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond("401: Password or Username incorrect")
        }

    }

    authenticationRoutes()
    contentRoutes()

}