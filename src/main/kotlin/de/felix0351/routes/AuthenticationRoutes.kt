package de.felix0351.routes

import de.felix0351.models.objects.UserSession
import de.felix0351.plugins.withInjection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


fun Route.login() = withInjection { service ->

    authenticate("form") {
        post("/login") {
            val username = call.principal<UserIdPrincipal>()?.name.toString()
            call.sessions.set(UserSession(username))
            call.respond(HttpStatusCode.Accepted, username)
        }
    }

}

fun Route.logout() = withInjection { service ->

    authenticate("session") {
        post("/user/logout") {
            

        }
    }


}


fun Application.authenticationRoutes() {
    routing {
        login()
        logout()
    }
}