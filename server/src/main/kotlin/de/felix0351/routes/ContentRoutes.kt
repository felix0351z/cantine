package de.felix0351.routes

import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Content
import de.felix0351.plugins.asBsonObjectId
import de.felix0351.plugins.withRole
import de.felix0351.repository.ContentRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private fun Route.with(route: Route.(repo: ContentRepository) -> Unit) {
    val contentRepo: ContentRepository by inject()
    route(contentRepo)
}

/**
 *  Get all available meals
 *  GET /content/meals
 *
 */
fun Route.meals() = with { contentRepo ->

    get("/meals") {

        val meals = contentRepo.getMeals()
        call.respond(HttpStatusCode.OK, meals)
    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST/PUT /content/meal
 * GET/DELETE/ /content/meal/<id>
 *
 */
fun Route.meal() = with { contentRepo ->

    route("/meal") {
        // Create a meal
        post {
            //Worker Permission is needed
            withRole(Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                contentRepo.addMeal(meal)

                call.respond(HttpStatusCode.OK, meal.id.toString())
            }


        }

        // Update a meal
        put {
            //Worker Permission is needed
            withRole(Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                contentRepo.updateMeal(meal)

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/{id}") {


            // Get the meal <id>
            get {

                val id = call.parameters["id"]!!
                val meal = contentRepo.getMeal(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK, meal)

            }

            //Remove the meal <id>
            delete {

                val id = call.parameters["id"]!!
                //Worker Permission is needed
                withRole(Auth.PermissionLevel.WORKER) {
                    contentRepo.deleteMeal(id.asBsonObjectId())
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
fun Route.reports() = with { contentRepo ->
    get("/reports") {

        val reports = contentRepo.getReports()
        call.respond(HttpStatusCode.OK, reports)

    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST /content/report
 * GET/DELETE/PUT /content/report/<id>
 *
 */
fun Route.report() = with { contentRepo ->
    route("/report") {

        // Add a report
        post {
            //Worker permission is needed
            withRole(Auth.PermissionLevel.WORKER) {

                val report = call.receive<Content.Report>()
                contentRepo.addReport(report)

                call.respond(HttpStatusCode.OK, report.id.toString())

            }
        }

        // Update a report
        put {
            //Worker permission is needed
            withRole(Auth.PermissionLevel.WORKER) {

                val report = call.receive<Content.Report>()
                contentRepo.updateReport(report)

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/{id}") {

            // Get the report <id>
            get {

                val id = call.parameters["id"]!!
                val report = contentRepo.getReport(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK, report)
            }

            // Delete the report <id>
            delete {

                val id = call.parameters["id"]!!
                // Worker permission needed
                withRole(Auth.PermissionLevel.WORKER) {

                    contentRepo.deleteReport(id.asBsonObjectId())
                    call.respond(HttpStatusCode.OK)
                }

            }
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