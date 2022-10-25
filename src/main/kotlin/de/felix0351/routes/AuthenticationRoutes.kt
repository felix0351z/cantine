package de.felix0351.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.login() {

}

fun Route.logout() {

}


fun Application.authenticationRoutes() {
    routing {
        login()
        logout()
    }
}