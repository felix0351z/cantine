package de.felix0351.routes

import de.felix0351.models.objects.UserSession
import io.ktor.server.application.*
import io.ktor.server.routing.*




fun Route.handleUserSession(function: (session: UserSession) -> Unit) {
    TODO("Not implemented yet")
}

fun Application.contentRoutes() {

}