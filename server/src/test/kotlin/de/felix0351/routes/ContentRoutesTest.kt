package de.felix0351.routes

import de.felix0351.https
import de.felix0351.json
import de.felix0351.login
import de.felix0351.models.objects.Content
import de.felix0351.objects.Meal
import de.felix0351.objects.Report
import de.felix0351.objects.exampleMeal
import de.felix0351.objects.exampleReport
import de.felix0351.testModule
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

class ContentRoutesTest {



    @Test
    fun testGetMeals() = testModule {
        it.login()

        val millis = measureTimeMillis {
            val meals: List<Meal> = it.get("/content/meals") {
                https()
            }.body()

            println(meals)
        }
        println("Taken time: $millis ms")
    }

    @Test
    fun testGetMeal() = testModule {
        it.login()

        val id = "63aefa039f5216472e65dfb7"
        val meal: Meal = it.get("/content/meal/$id") {
            https()
        }.body()

        println(meal)
    }

    @Test
    fun testAddMeal() = testModule {
        it.login()
        val meal = Json.encodeToString(exampleMeal)
        println(meal)
        
        val millis = measureTimeMillis {
            val id = it.post("/content/meal") {
                https()
                json(exampleMeal)
            }.bodyAsText()
            println(id)
        }

        println("Taken time: $millis ms")
    }

    @Test
    fun testUpdateMeal() = testModule {
        it.login()

        val response = it.put("/content/meal") {
            https()
            json(exampleMeal.copy(id = "63aefb63e24dbe74936c8b60", price = 100F))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testDeleteMeal() = testModule {
        it.login()

        val id = "63aefa039f5216472e65dfb7"
        val response = it.delete("/content/meal/$id") {
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


    @Test
    fun testGetReports() = testModule {
        it.login()

        val reports: List<Report> = it.get("/content/reports") {
            https()
        }.body()

        println(reports)
    }

    @Test
    fun testGetReport() = testModule {
        it.login()

        val id = "63af09ddcc84482c5951d945"
        val report: Report = it.get("/content/report/$id") {
            https()
        }.body()

        println(report)
    }

    @Test
    fun testAddReport() = testModule {
        it.login()

        val id = it.post("/content/report") {
            https()
            json(exampleReport)
        }.bodyAsText()

        println(id)
    }

    @Test
    fun testUpdateReport() = testModule {
        it.login()

        val response = it.put("/content/report") {
            https()
            json(exampleReport.copy(id = "63af09ddcc84482c5951d945", description = "Nichts wichtiges"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testDeleteReport() = testModule {
        it.login()

        val id = "63af09ddcc84482c5951d945"
        val response = it.delete("/content/report/$id") {
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun addCategory() = testModule {
        it.login()

        val category = Content.Category(name = "Mittagessen")
        val response = it.post("/content/category") {
            https()
            json(category)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getCategories() = testModule {
        it.login()

        val list: List<Content.Category> = it.get("/content/categories") {
            https()
        }.body()

        println(list)
    }

    @Test
    fun deleteCategory() = testModule {
        it.login()

        val category = "Mittagessen"
        val response = it.delete("/content/category") {
            https()
            json(category)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun addSelection() = testModule {
        it.login()

        val category = Content.SelectionGroup(
            name = "Soße",
            elements = listOf(
                Content.Selection(
                    name = "Ketchup",
                    price = 0.5F
                ),
                Content.Selection(
                    name = "Senf",
                    price = 0.5F
                ),
                Content.Selection(
                    name = "Ketchup",
                    price = 0.5F
                )
            )
        )

        val response = it.post("/content/selection") {
            https()
            json(category)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getSelections() = testModule {
        it.login()

        val list: List<Content.SelectionGroup> = it.get("/content/selections") {
            https()
        }.body()

        println(list)
    }

    @Test
    fun deleteSelection() = testModule {
        it.login()

        val name = "Soße"
        val response = it.delete("/content/selection") {
            https()
            json(name)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


}