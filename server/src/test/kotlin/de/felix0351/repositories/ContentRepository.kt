package de.felix0351.repositories

import de.felix0351.getContentRepo
import de.felix0351.models.objects.Content
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant
import java.util.*
import kotlin.test.assertTrue

class ContentRepository {

    private val category = Content.Category(
        id = null,
        name = "Mittagessen"
    )
    private val meal = Content.Meal(
        id = null,
        category = category,
        name = "Pommes",
        description = "Frittierte Kartoffeln in Stangenform lol",
        price = 2.2F,
        deposit = null,
        day = null,
        selections = listOf(
            Content.SelectionGroup(
                name = "Soße",
                elements = listOf(
                    Content.Selection("Ketchup", 0.1F),
                    Content.Selection("Rahmsoße", 0.2F)
                )
            )
        ),
        picture = "/var/blabla"
    )
    val order = Content.Order(
        id = null,
        code = UUID.randomUUID(),
        user = "felix0351",
        meals = listOf(
            Content.OrderedMeal(
                name = "pommes",
                description = "kartoffeln",
                price = 2F,
                deposit = null,
                day = null,
                selections = listOf("Rahmsoße"),
                picture = "/var/blabla"
            )

        ),
        orderTime = Instant.now()
    )
    val report = Content.Report(
        id = null,
        title = "Neuer Report",
        description = "Wieder Freitag geschlossen",
        picture = "/var/blabla",
        creationTime = Instant.now()
    )

    //Categories

    @Test
    fun addCategory() = runBlocking{

        getContentRepo().addCategory(category)
    }

    @Test
    fun getCategories() = runBlocking {
        val list = getContentRepo().getCategories()
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun deleteCategory() = runBlocking {
        getContentRepo().deleteCategory("Mittagessen")
    }

    //Meals

    @Test
    fun addMeal() = runBlocking {
        getContentRepo().addMeal(meal)
    }

    @Test
    fun getMeals() = runBlocking {
        val list = getContentRepo().getMeals()
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun updateMeal() = runBlocking {
        val repo = getContentRepo()

        val firstMeal = repo.getMeals()[0].copy(price = 5.5F)
        getContentRepo().updateMeal(firstMeal)
    }

    @Test
    fun deleteMeal() = runBlocking {
        val repo = getContentRepo()

        val first = repo.getMeals()[0]
        repo.deleteMeal(first.id!!)
    }

    //Orders

    @Test
    fun addOrder() = runBlocking {
        getContentRepo().addOrder(order)
    }

    @Test
    fun getOrdersFromUser() = runBlocking {
        val list = getContentRepo().getOrdersFromUser("felix0351")
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun deleteOrder() = runBlocking {
        val repo = getContentRepo()

        val first = repo.getOrdersFromUser("felix0351")[0]
        repo.deleteOrder(first.id!!)
    }


    //Reports

    @Test
    fun addReports() = runBlocking {
        getContentRepo().addReport(report)
    }

    @Test
    fun getReports() = runBlocking {
        val list = getContentRepo().getReports()
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun updateReport() = runBlocking {
        val repo = getContentRepo()

        val newReport = repo.getReports()[0].copy(description = "Donnerstags geschlossen")
        getContentRepo().updateReport(newReport)
    }

    @Test
    fun deleteReport() = runBlocking {
        val repo = getContentRepo()

        val first = repo.getReports()[0]
        repo.deleteReport(first.id!!)
    }






}