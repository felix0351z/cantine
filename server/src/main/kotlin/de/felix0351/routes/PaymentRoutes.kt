package de.felix0351.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*


/**
 * Create a new order
 * POST /payment/order
 *
 * Minimum Permission Level: User
 *
 */
fun Route.createOrder() {
    post("/order") {


    }
}

/**
 * Cancel an own order
 * DELETE /payment/order/
 *
 * Minimum Permission Level: User (Only his own orders)
 */
fun Route.cancelOrder() {
    delete("/order") {
        //ID in Body


    }
}


/**
 * Get all self taken purchases
 * GET /payment/purchases
 *
 * Minimum Permission Level: User
 */
fun Route.getPurchases() {
    get("/purchases") {

    }
}


/**
 * Delete own purchases, or from all users (Admin)
 * 1. DELETE /payment/purchases
 * Minimum Permission Level: User
 *
 * 2. DELETE /payment/purchases/all
 * Minimum Permission Level: Admin
 *
 *
 */
fun Route.deletePurchases() {
    authenticate("session") {

        delete("/purchases") {  }

        delete("/purchases/all") {

        }

    }

}


/**
 * Verify a purchase at the mensa
 *
 * POST /payment/purchase/
 *
 * Minimum Permission Level: Worker
 *
 */
fun Route.verifyPurchase() {
    post("/purchase/") {

        //TODO: JSON-Content

    }

    //



}

fun Application.paymentRoutes() {
    routing {
        authenticate("sessions") {
            route("/payment") {
                createOrder()
                cancelOrder()

                getPurchases()
                deletePurchases()

                verifyPurchase()

            }
        }
    }
}
