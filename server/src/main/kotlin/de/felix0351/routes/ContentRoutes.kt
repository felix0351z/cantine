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
        // Get the meal <id>
        get {

            val id = call.receive<String>()
            val meal = contentRepo.getMeal(id.asBsonObjectId())

            call.respond(HttpStatusCode.OK, meal)

        }


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

        //Remove the meal
        delete {
            //Worker Permission is needed
            withRole(Auth.PermissionLevel.WORKER) {
                val id = call.receive<String>()
                contentRepo.deleteMeal(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK)
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

        // Get the report
        get {

            val id = call.receive<String>()
            val report = contentRepo.getReport(id.asBsonObjectId())

            call.respond(HttpStatusCode.OK, report)
        }

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

        // Delete the report
        delete {

            val id = call.receive<String>()
            // Worker permission needed
            withRole(Auth.PermissionLevel.WORKER) {

                contentRepo.deleteReport(id.asBsonObjectId())
                call.respond(HttpStatusCode.OK)
            }

        }


    }
}

/**
 *  Get all category templates
 *  GET /content/categories
 *
 */
fun Route.categories() = with { repo ->
    get("/categories") {
        withRole(Auth.PermissionLevel.WORKER) {
            val categories = repo.getCategories()

            call.respond(HttpStatusCode.OK, categories)
        }
    }
}

/**
 *  Add or delete a category template
 *
 *  POST/DELETE /content/category
 *
 *
 */
fun Route.category() = with { repo ->
    route("/category") {
        post {
            withRole(Auth.PermissionLevel.WORKER) {
                val category = call.receive<Content.Category>()
                repo.addCategory(category)

                call.respond(HttpStatusCode.OK, category.name)
            }

        }
        delete {
            withRole(Auth.PermissionLevel.WORKER) {
                val categoryName = call.receive<String>()
                repo.deleteCategory(categoryName)

                call.respond(HttpStatusCode.OK)
            }

        }


    }

}

/**
 * Get all selection templates
 * GET /content/selections
 */
fun Route.selections() = with { repo ->
    get("/selections") {
        withRole(Auth.PermissionLevel.WORKER) {
            val selections = repo.getSelections()
            call.respond(HttpStatusCode.OK, selections)
        }
    }
}

/**
 * Add/Delete a selection template
 * POST/DELETE /content/selection
 *
 */
fun Route.selection() = with { repo ->
    route("/selection") {
        post {
            withRole(Auth.PermissionLevel.WORKER){
                val selections = call.receive<Content.SelectionGroup>()
                repo.addSelections(selections)

                call.respond(HttpStatusCode.OK, selections.name)
            }
        }

        delete {
            withRole(Auth.PermissionLevel.WORKER) {
                val selectionName = call.receive<String>()
                repo.deleteSelection(selectionName)

                call.respond(HttpStatusCode.OK)
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
                categories()
                category()
                selections()
                selection()
            }
        }



    }
}