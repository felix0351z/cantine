package de.felix0351.plugins


import de.felix0351.exceptions.AuthenticationException
import de.felix0351.exceptions.AuthorizationException
import de.felix0351.routes.authenticationRoutes
import de.felix0351.routes.contentRoutes

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.response.*


//TODO Configure StatusPages
fun Application.configureRouting() {

    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

    }

    authenticationRoutes()
    contentRoutes()

}
