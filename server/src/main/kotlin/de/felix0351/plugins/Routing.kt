package de.felix0351.plugins

import de.felix0351.models.errors.*
import de.felix0351.routes.authenticationRoutes
import de.felix0351.routes.contentRoutes
import de.felix0351.routes.paymentRoutes
import de.felix0351.utils.LoggingPlugin
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.partialcontent.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.slf4j.event.*
import kotlin.reflect.typeOf

const val BASE_ROUTE = "/api"

/**
 * Install the routing plugins, define all routes and load all necessary status pages
 */
fun Application.configureRouting() {
    install(XForwardedHeaders)
    install(PartialContent)
    install(LoggingPlugin)

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

        exception<FileIOException> { call, _ ->
            call.respond(
                HttpStatusCode.InternalServerError, RouteError(
                    id = ErrorCode.FileIOException.code,
                    description = "Failed to write the provided file into the storage"
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

        exception<BadRequestException> { call, err ->
            call.respond(
                HttpStatusCode.BadRequest, RouteError(
                    id = ErrorCode.ContentTransformationError.code,
                    description = "The data of the request isn't correct: ${err.message}"
                )
            )
        }


    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Cantine Server is running")
        }

        route(BASE_ROUTE) {

            authenticationRoutes()
            contentRoutes()
            paymentRoutes()

        }

    }

}

/**
* Transform a string into a bson object id
 * @throws IllegalIdException If the string is not a valid id
 * @see Id
*/
@Throws(IllegalIdException::class)
fun <T> String.asBsonObjectId(): Id<T> {
    try {
        return ObjectId(this).toId()
    } catch (ex: IllegalArgumentException) {
        throw IllegalIdException()
    }
}

/**
 * Receive a multipart request and extract the json body and the image file to items
 * @param func The handling function for the received request
 */
suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.receiveRequestWithImage(
    func: PipelineContext<Unit, ApplicationCall>.(body: T, image: PartData.FileItem?) -> Unit
) {
    val multipart = call.receiveMultipart()

    var json: String? = null
    var content: PartData.FileItem? = null
    var count = 0

    multipart.forEachPart {
        when (it) {
            is PartData.FileItem -> {
                if (it.contentType != ContentType.Image.JPEG) throw BadRequestException("Unknown content type")
                content = it
                count++
            }

            is PartData.FormItem -> {
                if (it.contentType != ContentType.Application.Json) throw BadRequestException("Unknown content type")
                json = it.value
                count++
            }

            else -> {
                throw BadRequestException("Unknown PartData provided")
            }
        }
    }

    if ((json != null) && (count <= 2)) {

        val body: T = try {
            Json.decodeFromString(json!!)
        } catch (ex: RuntimeException) {
            throw CannotTransformContentToTypeException(typeOf<T>())
        }

        func(body, content)
    } else {
        throw BadRequestException("A MultipartRequest must provide one json FormItem and only one image FileItem. You provided $count items!")
    }
}

