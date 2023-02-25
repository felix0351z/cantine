package de.felix0351.plugins

import de.felix0351.models.errors.*
import de.felix0351.routes.authenticationRoutes
import de.felix0351.routes.contentRoutes
import de.felix0351.routes.paymentRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.*
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId

fun Application.configureRouting() {
    install(XForwardedHeaders)

    install(StatusPages) {

        exception<DatabaseException> { call, databaseException ->
            when (databaseException) {
                is DatabaseException.InternalException -> {
                    call.respond(
                        HttpStatusCode.InternalServerError, RouteError(
                            id = ErrorCode.InternalDatabaseError.code,
                            description = "Error in internal database occurred"
                        )
                    )
                }

                is DatabaseException.NotFoundException -> {
                    call.respond(
                        HttpStatusCode.NotFound, RouteError(
                            id = ErrorCode.NotFound.code,
                            description = "Requested value wasn't found"
                        )
                    )
                }

                is DatabaseException.SameValueException -> {
                    call.respond(
                        HttpStatusCode.Conflict, RouteError(
                            id = ErrorCode.SameValue.code,
                            description = "Value is already the same as requested"
                        )
                    )
                }

                is DatabaseException.ValueAlreadyExistsException -> {
                    call.respond(
                        HttpStatusCode.Conflict, RouteError(
                            id = ErrorCode.AlreadyExists.code,
                            description = "Value already exists"
                        )
                    )
                }
            }

        }

        exception<IllegalIdException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.IllegalID.code,
                    description = "No valid id"
                )
            )
        }

        exception<WrongPasswordException> { call, _ ->
            call.respond(
                HttpStatusCode.Forbidden, RouteError(
                    id = ErrorCode.WrongPassword.code,
                    description = "Wrong password"
                )
            )
        }

        exception<NoPasswordException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.NoPassword.code,
                    description = "Password is needed"
                )
            )
        }

        exception<NotEnoughMoneyException> { call, err ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.NotEnoughMoney.code,
                    description = "You are ${err.minus} to low to create this order!"
                )
            )
        }


        // Kotlinx Transformation errors

        exception<ContentTransformationException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.ContentTransformationError.code,
                    description = "Received content can't be transformed into an object"
                )
            )
        }

        exception<BadRequestException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.ContentTransformationError.code,
                    description = "The data of the request isn't correct"
                )
            )


        }


    }

    authenticationRoutes()
    contentRoutes()
    paymentRoutes()

}

@Throws(IllegalIdException::class)
fun <T> String.asBsonObjectId(): Id<T> {
    try {
        return ObjectId(this).toId()
    } catch (ex: IllegalArgumentException) {
        throw IllegalIdException()
    }
}

