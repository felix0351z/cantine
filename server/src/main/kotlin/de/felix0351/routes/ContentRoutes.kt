package de.felix0351.routes

import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Content
import de.felix0351.plugins.asBsonObjectId
import de.felix0351.plugins.checkPermission
import de.felix0351.plugins.withInjection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


/**
 *  Get all available meals
 *  GET /content/meals
 *
 */
fun Route.meals() = withInjection { service ->
    get("/meals") {

        val meals = service.contentRepo.getMeals()
        call.respond(HttpStatusCode.OK, meals)
    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST/PUT /content/meal
 * GET/DELETE/ /content/meal/<id>
 *
 */
fun Route.meal() = withInjection { service ->
    route("/meal") {
        // Create a meal
        post {
            //Worker Permission is needed
            checkPermission(Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                service.contentRepo.addMeal(meal)

                call.respond(HttpStatusCode.OK, meal.id.toString())
            }


        }

        // Update a meal
        put {
            //Worker Permission is needed
            checkPermission(Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                service.contentRepo.updateMeal(meal)

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/{id}") {


            // Get the meal <id>
            get {

                val id = call.parameters["id"]!!
                val meal = service.contentRepo.getMeal(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK, meal)

            }

            //Remove the meal <id>
            delete {

                val id = call.parameters["id"]!!
                //Worker Permission is needed
                checkPermission(Auth.PermissionLevel.WORKER) {
                    service.contentRepo.deleteMeal(id.asBsonObjectId())

                    call.respond(HttpStatusCode.OK)
                }

            }
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