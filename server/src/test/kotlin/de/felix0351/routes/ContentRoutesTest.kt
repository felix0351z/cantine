package de.felix0351.routes


import de.felix0351.https
import de.felix0351.json
import de.felix0351.login
import de.felix0351.models.objects.Content
import de.felix0351.objects.*
import de.felix0351.testModule
import de.felix0351.utils.getLogger
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.test.Test

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ContentRoutesTest {

    companion object {
        var mealID: String = ""
        var reportID: String = ""
        val selectionName = "Soße"
        val categoryName = "Mittagessen"
    }


    @Test
    @Order(1)
    fun testAddMeal() = testModule {
        it.login()
        val meal = Json.encodeToString(exampleMeal)

        val millis = measureTimeMillis {
            val id = it.post("/content/meal") {
                setBody(MultiPartFormDataContent(
                    parts = formData {
                        append("image", File(testImage).readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"test-file.jpg\"")
                        })
                        append(FormPart("json", meal, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        }))
                    }
                ))
                https()
                onUpload { bytesSentTotal, contentLength ->
                    getLogger().info("Sent $bytesSentTotal baytes from $contentLength")
                }

            }.bodyAsText()

            println(id)
            mealID = id
        }

        println("Taken time: $millis ms")
    }

    @Test
    @Order(2)
    fun testGetImage() = testModule {
        it.login()

        val response = it.get("/content/image/MEALS/$mealID") {
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
        withContext(Dispatchers.IO) {
            val file =File("testImage.jpg")
            file.createNewFile()
            file.writeBytes(response.readBytes())
        }
    }

    @Test
    @Order(3)
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
    @Order(4)
    fun testUpdateMeal() = testModule {
        it.login()
        val body = Json.encodeToString(exampleMeal.copy(id = mealID, price = 100F))

        val response = it.put("/content/meal") {
            setBody(MultiPartFormDataContent(
                parts = formData {
                    append(FormPart("json", body, Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }
            ))
            https()
        }

        assertEquals( HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(5)
    fun testGetMeal() = testModule {
        it.login()

        val meal: Meal = it.get("/content/meal") {
            https()
            json(mealID)
        }.body()

        println(meal)
    }


    @Test
    @Order(6)
    fun testDeleteMeal() = testModule {
        it.login()

        val response = it.delete("/content/meal") {
            https()
            json(mealID)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    //Reports

    @Test
    @Order(7)
    fun testAddReport() = testModule {
        it.login()

        val report = Json.encodeToString(exampleReport)

        val id = it.post("/content/report") {
            setBody(MultiPartFormDataContent(
                parts = formData {
                    append("image", File(testImage).readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"test-file.jpg\"")
                    })
                    append(FormPart("json", report, Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }
            ))
            https()
        }.bodyAsText()

        println(id)
        reportID = id
    }


    @Test
    @Order(8)
    fun testGetReports() = testModule {
        it.login()

        val reports: List<Report> = it.get("/content/reports") {
            https()
        }.body()

        println(reports)
    }

    @Test
    @Order(9)
    fun testUpdateReport() = testModule {
        it.login()

        val report = Json.encodeToString(exampleReport.copy(id = reportID, description = "Nichts wichtiges"))

        val response = it.put("/content/report") {

            setBody(MultiPartFormDataContent(
                parts = formData {
                    append(FormPart("json", report, Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }))
                }

            ))
            https()
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(10)
    fun testGetReport() = testModule {
        it.login()

        val report: Report = it.get("/content/report") {
            https()
            json(reportID)
        }.body()

        println(report)
    }

    @Test
    @Order(11)
    fun testDeleteReport() = testModule {
        it.login()

        val response = it.delete("/content/report") {
            https()
            json(reportID)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


    @Test
    @Order(12)
    fun addCategory() = testModule {
        it.login()

        val category = Content.Category(name = categoryName)
        val response = it.post("/content/category") {
            https()
            json(category)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(13)
    fun getCategories() = testModule {
        it.login()

        val list: List<Content.Category> = it.get("/content/categories") {
            https()
        }.body()

        println(list)
    }

    @Test
    @Order(14)
    fun deleteCategory() = testModule {
        it.login()

        val response = it.delete("/content/category") {
            https()
            json(categoryName)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(15)
    fun addSelection() = testModule {
        it.login()

        val category = Content.SelectionGroup(
            name = selectionName,
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
    @Order(16)
    fun getSelections() = testModule {
        it.login()

        val list: List<Content.SelectionGroup> = it.get("/content/selections") {
            https()
        }.body()

        println(list)
    }

    @Test
    @Order(17)
    fun deleteSelection() = testModule {
        it.login()

        val response = it.delete("/content/selection") {
            https()
            json(selectionName)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }


}