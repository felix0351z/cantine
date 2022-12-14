package de.felix0351.plugins

import de.felix0351.dependencies.CantineService
import de.felix0351.models.errors.ErrorCode
import de.felix0351.models.errors.RouteError
import de.felix0351.models.objects.Auth
import de.felix0351.utils.FileHandler
import io.ktor.http.*

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

import kotlin.collections.set
import kotlin.time.Duration.Companion.days


const val COOKIE_PATH = "/" //Path which needs a session

fun Application.configureSecurity() {
    val service by inject<CantineService>()

    install(Sessions) {
        configureAuthCookie()
    }

    install(Authentication) {
        configureFormAuthentication(service)
        configureSessionAuthentication()
    }

}


private fun SessionsConfig.configureAuthCookie() {
    val sessionAge = FileHandler.configuration.authentication.session_age
    val signKey = hex(FileHandler.configuration.authentication.sign_key)
    val authKey = hex(FileHandler.configuration.authentication.auth_key)

    cookie<Auth.UserSession>("user_session", SessionStorageMemory()) {

        // Only transfer cookies via ssl encrypted connection
        cookie.secure = true

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
private fun AuthenticationConfig.configureSessionAuthentication() {
    session<Auth.UserSession>("session") {

        // Additional validation not needed
        validate { session ->
            session
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

private fun AuthenticationConfig.configureFormAuthentication(service: CantineService) {

    form("form") {
        userParamName = "username"
        passwordParamName = "password"

        validate { credentials ->

            val user = service.checkUserCredentials(credentials.name, credentials.password)

            if (user != null) Auth.UserSession(user.username)
            else null
        }

        challenge {
            call.respond(HttpStatusCode.Unauthorized)
        }

    }
}

/**
 * Utility extended function to get the current session as Object
 *
 * @see Auth.UserSession
 */
fun PipelineContext<Unit, ApplicationCall>.currentSession(): Auth.UserSession? =
    call.sessions.get<Auth.UserSession>()





suspend fun PipelineContext<Unit, ApplicationCall>.currentUser(service: CantineService): Auth.User {
    val session = currentSession()
    return service.getPrivateUser(session!!)
}


suspend inline fun PipelineContext<Unit, ApplicationCall>.checkPermission(
    service: CantineService,
    minimum: Auth.PermissionLevel,
    route: PipelineContext<Unit, ApplicationCall>.(user: Auth.User) -> Unit
) {
    // If there is no session. Normally not null, because authorization block comes before this
    val session = call.sessions.get<Auth.UserSession>()
    if(session == null) {
        call.respond(HttpStatusCode.Forbidden)
        return
    }

    val user = service.getPrivateUser(session)

    // If the user has the minimum permission level
    if (user.permissionLevel.int >= minimum.int) {
        route(user)
    } else {
        call.respond(
            HttpStatusCode.Forbidden,
            RouteError(
                id = ErrorCode.NoPermission.code,
                description = "You don't have enough permissions to call this route. Minimum Permission for this is $minimum"
            )
        )
    }
}

