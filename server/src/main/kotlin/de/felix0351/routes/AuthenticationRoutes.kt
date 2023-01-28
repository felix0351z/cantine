package de.felix0351.routes

import de.felix0351.models.objects.*
import de.felix0351.plugins.withRole
import de.felix0351.services.AuthenticationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

private fun Route.with(route: Route.(service: AuthenticationService) -> Unit) {
    val service: AuthenticationService by inject()
    route(service)
}


fun Route.login() {
    authenticate("form") {
        post("/login") {
            val userToLogin = call.principal<Auth.UserSession>()!!

            //Create new session for the user
            call.sessions.set(userToLogin)
            call.respond(HttpStatusCode.Accepted, userToLogin.username)
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
 * Minimum Permission: Admin
 *
 */
fun Route.users() = with { service ->
    get("/users") {
        withRole(Auth.PermissionLevel.ADMIN) {

            val users = service.getUsers()
            call.respond(HttpStatusCode.OK, users)

        }
    }
}


/**
 *  Get Self -> GET /account
 *  Change own password -> POST /account/password
 *
 */
fun Route.account() = with { service ->
    route("/account") {

        get {
            val session = call.sessions.get<Auth.UserSession>()!!
            val user = service.getUser(session.username)

            call.respond(HttpStatusCode.OK, user)
        }

        post("/password") {

            withRole(Auth.PermissionLevel.USER) { user ->

                val request = call.receive<PasswordChangeRequest>()
                service.changeOwnPassword(user, request)
                call.respond(HttpStatusCode.OK)

            }
        }

    }


}

/**
 * Get/Delete or update a user
 *
 * GET /user/<id>
 * DELETE /user
 * POST /user/name
 * POST /user/password
 * POST /user/permission
 * 
 */
fun Route.user() = with { service ->
    route("/user") {

        // Get user
        get {

            withRole(Auth.PermissionLevel.ADMIN) {
                val name = call.receive<String>()
                call.respond(HttpStatusCode.OK, service.getUser(name))
            }

        }


        // Add new user
        post {
            withRole(Auth.PermissionLevel.ADMIN) { user ->
                val request = call.receive<UserAddRequest>()
                service.addUser(user, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Delete user
        delete {
            withRole(Auth.PermissionLevel.ADMIN) { user ->
                val request = call.receive<UserDeleteRequest>()
                service.deleteUser(user, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users name
        post("/name") {
            withRole(Auth.PermissionLevel.ADMIN) { user ->
                val request = call.receive<NameChangeRequest>()
                service.changeName(user, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users password
        post("/password") {
            withRole(Auth.PermissionLevel.ADMIN) { user ->
                val request = call.receive<PasswordChangeRequest>()
                service.changePassword(user, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users permission level
        post("/permission") {
            withRole(Auth.PermissionLevel.ADMIN) { user ->
                val request = call.receive<PermissionChangeRequest>()
                service.changePermissionLevel(user, request)

                call.respond(HttpStatusCode.OK)
            }
        }


    }
}






fun Application.authenticationRoutes() {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Cantine Server is running")
        }

        login()
        logout()

        authenticate("session") {
            account()
            users()
            user()
        }
    }
}