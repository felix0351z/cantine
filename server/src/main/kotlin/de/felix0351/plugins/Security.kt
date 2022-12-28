package de.felix0351.plugins

import de.felix0351.dependencies.CantineService
import de.felix0351.models.errors.ErrorCode
import de.felix0351.models.errors.ServiceError
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
import java.io.File

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

    cookie<Auth.UserSession>("user_session", directorySessionStorage(File(SESSION_FILE_PATH))) {

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

            if (user != null) Auth.UserSession(user)
            else null
        }

        challenge {
            call.respond(HttpStatusCode.Unauthorized)
        }

    }
}


suspend inline fun PipelineContext<Unit, ApplicationCall>.checkPermission(
    minimum: Auth.PermissionLevel,
    route: PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
    // If there is no session. Normally not null, because authorization block comes before this
    val session = call.sessions.get<Auth.UserSession>()
    if(session == null) {
        call.respond(HttpStatusCode.Forbidden)
        return
    }

    // If the user has the minimum permission level
    if (session.user.permissionLevel.int >= minimum.int) {
        route()
    } else {
        call.respond(
            HttpStatusCode.Forbidden,
            ServiceError(
                id = ErrorCode.NoPermission.code,
                description = "You don't have enough permissions to call this route. Minimum Permission for this is $minimum"
            )
        )
    }
}

