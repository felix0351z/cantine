package de.felix0351.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


/**
 *  Get all available meals
 *  GET /content/meals
 *
 *  Minimum permission level: User
 */
fun Route.getMeals() {
    get("/meals") {
        call.respond(HttpStatusCode.OK)
    }
}

/**
 * Get a specific meal
 * GET /content/meal/<id>
 *
 * Minimum Permission Level: User
 */
fun Route.getMeal() {
    get("/meal/{id}") {
        call.respond(HttpStatusCode.OK, call.parameters["id"].toString())
    }

}

/**
 *  Add new meal
 *  POST /content/meal
 *
 *  Minimum PermissionLevel: Worker
 *
 */
fun Route.addMeal() {
    post("/meal") {
        call.respond(HttpStatusCode.OK)
    }
}


/**
 * Delete/Edit one specific meal
 * DELETE/PUT /content/meal/<id>
 *
 * Minimum Permission Level: Worker
 *
 */
fun Route.deleteOrEditMeal() {

    route("/meal/{id}") {
        delete {
            call.respond(HttpStatusCode.OK, call.parameters["id"].toString())
        }
        put {
            call.respond(HttpStatusCode.OK, call.parameters["id"].toString())
        }
    }
}


/**
 * Get all reports
 * GET /content/reports
 *
 * Minimum Permission Level: User
 *
 */
fun Route.getReports() {
    get("/reports") {
        call.respond(HttpStatusCode.OK)
    }
}


/**
 * Get specific report
 * GET /content/report/<id>
 *
 * Minimum Permission Level: User
 */
fun Route.getReport() {
    get("/report/{id}") {
        call.respond(HttpStatusCode.OK, call.parameters["id"].toString())
    }
}

/**
 * Add new report
 * POST /content/report/
 *
 * Minimum Permission Level: Worker
 */
fun Route.addReport() {
    post("/report") {

    }
}

/**
 * Delete/Edit one specific report
 * DELETE/PUT /content/report/<id>
 *
 * Minimum Permission Level: Worker
 *
 */
fun Route.deleteOrEditReport() {
    route("/report/{id}") {
        delete {  }
        post {  }


    }

}



fun Application.contentRoutes() {
    routing {

        authenticate("session") {
            route("/content") {
                getMeals()
                getMeal()
                addMeal()
                deleteOrEditMeal()

                getReports()
                getReport()
                addReport()
                deleteOrEditReport()
            }
        }



    }
}