package de.felix0351.routes

import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.CreateOrderRequest
import de.felix0351.models.objects.DeleteOrderRequest
import de.felix0351.plugins.checkPermission
import de.felix0351.plugins.currentSession
import de.felix0351.plugins.withInjection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Credit of the user
 * GET/POST /payment/credit
 *
 *
 */

fun Route.credit() = withInjection { service ->
    route("/credit") {
        get {
            val session = currentSession()!!
            val credit = service.getUser(session.username).credit

            call.respond(HttpStatusCode.OK, credit)
        }

        post {
            //Worker permission

            checkPermission(service, Auth.PermissionLevel.WORKER) {
                TODO()
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
fun Route.orders() = withInjection {service ->
    get("/orders") {

        val session = currentSession()!!
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
fun Route.order() = withInjection { service ->
    route("/order") {
        post {
            val request = call.receive<CreateOrderRequest>()
            val session = currentSession()!!

            service.createOrder(session.username, request)
            call.respond(HttpStatusCode.OK)
        }
        delete {
            val request = call.receive<DeleteOrderRequest>()
            val session = currentSession()!!

            service.cancelOrder(session.username, request)
            call.respond(HttpStatusCode.OK)
        }
    }
}


/**
 * Get/Clear all purchases
 * GET/DELETE /payment/purchases
 *
 * Minimum Permission Level: User
 */
fun Route.purchases() = withInjection { service ->
    route("/purchases") {
        get {
            val session = currentSession()!!
            val payments = service.getPayments(session.username)

            call.respond(HttpStatusCode.OK, payments)
        }
        delete {
            val session = currentSession()!!
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
fun Route.purchase() {
    post("/purchase/") {

        //TODO: JSON-Content

    }

    //



}

fun Application.paymentRoutes() {
    routing {

        // All payment routes needs an active user session
        authenticate("sessions") {
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
