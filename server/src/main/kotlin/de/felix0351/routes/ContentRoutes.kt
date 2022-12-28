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
 */
fun Route.meals() {
    get("/meals") {

    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST /content/meal
 * GET/DELETE/PUT /content/meal/<id>
 *
 */
fun Route.meal() {
    route("/meal") {
        post {

        }

        route("/{id}") {
            get {  }
            delete {  }
            put {  }
        }
    }
}


/**
 * Get all reports
 * GET /content/reports
 *
 *
 */
fun Route.reports() {
    get("/reports") {
        call.respond(HttpStatusCode.OK)
    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST /content/report
 * GET/DELETE/PUT /content/report/<id>
 *
 */
fun Route.report() {
    route("/report") {
        post {  }

        route("/{id}") {
            get {  }
            delete {  }
            put {  }
        }
    }
}



fun Application.contentRoutes() {
    routing {

        // All content routes need a active user session
        authenticate("session") {
            route("/content") {
                meals()
                meal()
                reports()
                report()
            }
        }



    }
}