package de.felix0351.routes

import de.felix0351.models.objects.Auth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


fun Route.login() {
    authenticate("form") {
        post("/login") {
            val userToLogin = call.principal<Auth.UserSession>()!!

            //Create new session for the user
            call.sessions.set(userToLogin)
            call.respond(HttpStatusCode.Accepted, userToLogin.user.username)
        }
    }

}

fun Route.logout() {
    authenticate("session") {

        get("/logout") {
            call.sessions.clear<Auth.UserSession>()

            call.respond(HttpStatusCode.OK, "Logout successfully")
        }
    }


}

/**
 * Get all available users
 * GET /users
 *
 *
 *
 */
fun Route.users() {
    get("/users") {


    }
}

/**
 * Get/Delete or update a user
 *
 * GET/DELETE /user/<id>
 * POST /user/<id>/name
 * POST /user/<id>/password
 * POST /user/<id>/permission
 * 
 */
fun Route.user() {
    route("/user/{id}") {
        get {  }
        delete {  }

        post("/name") {  }
        post("/password") {  }
        post("/permission") {  }
    }

}






fun Application.authenticationRoutes() {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Cantine Server is running")
        }

        login()
        logout()
        users()
        user()
    }
}