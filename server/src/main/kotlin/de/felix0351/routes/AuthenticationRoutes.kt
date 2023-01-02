package de.felix0351.routes

import de.felix0351.models.objects.*
import de.felix0351.plugins.checkPermission
import de.felix0351.plugins.currentSession
import de.felix0351.plugins.withInjection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


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
fun Route.users() = withInjection { service ->
    get("/users") {
        checkPermission(service, Auth.PermissionLevel.ADMIN) {

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
fun Route.account() = withInjection { service ->
    route("/account") {

        get {
            val session = currentSession()!!
            val user = service.getUser(session.username)

            call.respond(HttpStatusCode.OK, user)
        }

        post("/password") {
            val request = call.receive<PasswordChangeRequest>()
            val session = currentSession()!!

            service.changeOwnPassword(session, request)
            call.respond(HttpStatusCode.OK)
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
fun Route.user() = withInjection { service ->
    route("/user") {

        // Get user
        get("/{username}") {

            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val name = call.parameters["username"]!!
                call.respond(HttpStatusCode.OK, service.getUser(name))
            }

        }


        // Add new user
        post {
            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val request = call.receive<UserAddRequest>()
                service.addUser(currentSession()!!, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Delete user
        delete {
            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val request = call.receive<UserDeleteRequest>()
                service.deleteUser(currentSession()!!, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users name
        post("/name") {
            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val request = call.receive<NameChangeRequest>()
                service.changeName(currentSession()!!, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users password
        post("/password") {
            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val request = call.receive<PasswordChangeRequest>()
                service.changePassword(currentSession()!!, request)

                call.respond(HttpStatusCode.OK)
            }
        }

        // Change users permission level
        post("/permission") {
            checkPermission(service, Auth.PermissionLevel.ADMIN) {
                val request = call.receive<PermissionChangeRequest>()
                service.changePermissionLevel(currentSession()!!, request)

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