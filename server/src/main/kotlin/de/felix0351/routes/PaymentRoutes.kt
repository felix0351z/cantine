package de.felix0351.routes

import de.felix0351.models.objects.*
import de.felix0351.plugins.withRole
import de.felix0351.services.PaymentService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.Id


private fun Route.with(route: Route.(service: PaymentService) -> Unit) {
    val service: PaymentService by inject()
    route(service)
}

/**
 * Credit of the user
 * GET/POST /payment/credit
 *
 *
 */
fun Route.credit() = with { service ->

route("/credit") {
        get {

            withRole(Auth.PermissionLevel.USER) {

                val credit = service.getCreditFromUser(it)
                call.respond(HttpStatusCode.OK, credit)

            }

        }

        post {
            //Worker permission

            withRole(Auth.PermissionLevel.WORKER) {

                val request = call.receive<AddCreditRequest>()
                service.addCreditToUser(it, request)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}


/**
 * All active orders
 * GET /payment/orders
 *
 *
 */
fun Route.orders() = with { service ->
    get("/orders") {

        val session = call.sessions.get<Auth.UserSession>()!!
        val orders = service.getOrdersFromUser(session.username)
        call.respond(HttpStatusCode.OK, orders)
    }
}



/**
 * Create/cancel an order
 * POST/DELETE /payment/order
 *
 *
 */
fun Route.order() = with { service ->
    route("/order") {
        post {

            withRole(Auth.PermissionLevel.USER) {
                val request = call.receive<CreateOrderRequest>()
                val order = service.createOrder(it, request)

                call.respond(HttpStatusCode.OK, order)
            }

        }
        delete {

            withRole(Auth.PermissionLevel.USER) {
                val id = call.receive<Id<Content.Order>>()

                service.cancelOrder(it, id)
                call.respond(HttpStatusCode.OK)
            }

        }
    }
}


/**
 * Get/Clear all purchases
 * GET/DELETE /payment/purchases
 *
 * Minimum Permission Level: User
 */
fun Route.purchases() = with { service ->
    route("/purchases") {
        get {
            val session = call.sessions.get<Auth.UserSession>()!!
            val payments = service.getPayments(session.username)

            call.respond(HttpStatusCode.OK, payments)
        }
        delete {
            val session = call.sessions.get<Auth.UserSession>()!!
            service.clearPayments(session.username)

            call.respond(HttpStatusCode.OK)
        }
    }
}


/**
 * Verify a purchase at the mensa
 * POST /payment/purchase/
 *
 */
fun Route.purchase() = with { service ->
    post("/purchase") {

        withRole(Auth.PermissionLevel.WORKER) {
            val request = call.receive<VerifyOrderRequest>()
            service.verifyOrder(request)

            call.respond(HttpStatusCode.OK)
        }

    }

}

fun Application.paymentRoutes() {
    routing {

        // All payment routes needs an active user session
        authenticate("session") {
            route("/payment") {
                credit()
                orders()
                order()
                purchases()
                purchase()
            }
        }
    }
}
