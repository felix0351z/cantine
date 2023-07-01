package de.felix0351.plugins

import de.felix0351.models.errors.ErrorCode
import de.felix0351.models.errors.RouteError
import de.felix0351.models.objects.Auth
import de.felix0351.services.AuthenticationService
import de.felix0351.utils.FileHandler
import io.ktor.http.*

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject
import java.io.File

import kotlin.collections.set
import kotlin.time.Duration.Companion.days


const val COOKIE_PATH = "/" //Path which needs a session

/**
 * Configure the authentication and authorization properties
 */
fun Application.configureSecurity() {
    val service by inject<AuthenticationService>()

    install(Sessions) {
        configureAuthCookie()
    }

    install(Authentication) {
        configureFormAuthentication(service)
        configureSessionAuthentication(service)
    }

}


private fun SessionsConfig.configureAuthCookie() {
    val sessionAge = FileHandler.configuration.authentication.session_age
    val signKey = hex(FileHandler.configuration.authentication.sign_key)
    val authKey = hex(FileHandler.configuration.authentication.auth_key)

    cookie<Auth.UserSession>("user_session", directorySessionStorage(File(FileHandler.SESSION_FILE_NAME))) {

        // Only transfer cookies via ssl encrypted connection, and only use false in the development process
        cookie.secure = false

        // Permanent session has a max age
        cookie.maxAge = sessionAge.days

        // Prevent cross-site request forgery attacks
        cookie.extensions["SameSite"] = "strict"

        // Path to the content, which needs a session
        cookie.path = COOKIE_PATH

        // Prevents user to edit the cookie, but can show the content
        transform(SessionTransportTransformerEncrypt(authKey, signKey))
    }
}

/**
 * Let the user log in bei their session (via cookie)
 * Checks if a User session exists to the cookie
 *
 * Return 403 if there is no valid session for request
 */
private fun AuthenticationConfig.configureSessionAuthentication(service: AuthenticationService) {
    session<Auth.UserSession>("session") {

        // Extra validation
        // Perform a database call to return the user as principal
        validate { session ->

            service.getPrivateUser(session)
        }

        challenge {
            call.respond(HttpStatusCode.Forbidden)
        }

    }
}

/**
 * Let the user sign in via a form based authentication
 * Username and password will be delivered in the attributes of a post request
 * Check`s if the credentials are correct and returns a Principal with the name
 * to the route if everything is fine
 *
 * Returns 401 if username or password is incorrect
 *
 */

private fun AuthenticationConfig.configureFormAuthentication(service: AuthenticationService) {

    form("form") {
        userParamName = "username"
        passwordParamName = "password"

        validate { credentials ->

            val user = service.checkUserCredentials(credentials.name, credentials.password)

            if (user != null) {
                application.log.info("User successfully logged in")
                Auth.UserSession(user.username)
            }
            else null
        }

        challenge {
            call.application.log.info("Password or username incorrect")
            call.respond(HttpStatusCode.Unauthorized, RouteError(
                id = ErrorCode.Unauthorized.code,
                description = "Password or Username incorrect"
            ))
        }

    }
}

/**
 * Check if the current user of the request has the necessary permission to call this route
 * @param minimum The minimum permission level to call this route
 * @return If the permission doesn't exist a NoPermission code will be responded
 * @see ErrorCode.NoPermission
 */
suspend inline fun PipelineContext<Unit, ApplicationCall>.withRole(
    minimum: Auth.PermissionLevel,
    route: PipelineContext<Unit, ApplicationCall>.(user: Auth.User) -> Unit
) {
    val user = call.principal<Auth.User>()!!

    // If the user has the minimum permission level
    if (user.permissionLevel.int >= minimum.int) {
        route(user)
    } else {
        application.log.info("Unauthorized call wit the permission level ${user.permissionLevel} to ${call.request.uri}")

        call.respond(
            HttpStatusCode.Forbidden,
            RouteError(
                id = ErrorCode.NoPermission.code,
                description = "You don't have enough permissions to call this route. Minimum Permission for this is $minimum"
            )
        )
    }
}

