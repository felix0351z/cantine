package de.felix0351.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

/**
 * Credit of the user
 * GET/POST /payment/credit
 *
 *
 */

fun Route.credit() {
    route("/credit") {
        get {  }

        post {
            //Worker permission

        }
    }
}


/**
 * All active orders
 * GET /payment/orders
 *
 *
 */
fun Route.orders() {
    get("/orders") {

    }
}



/**
 * Create/cancel an order
 * POST/DELETE /payment/order
 *
 *
 */
fun Route.order() {
    route("/order") {
        post {  }
        delete {  }
    }
}


/**
 * Get/Clear all purchases
 * GET/DELETE /payment/purchases
 *
 * Minimum Permission Level: User
 */
fun Route.purchases() {
    route("/purchases") {
        get {  }
        delete {  }
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
