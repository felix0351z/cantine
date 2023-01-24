package de.felix0351.repository

import de.felix0351.db.MongoDBConnection
import de.felix0351.models.errors.DatabaseException.*
import de.felix0351.models.objects.Content
import org.litote.kmongo.*

class ContentRepositoryMongoDB(private val con: MongoDBConnection) : ContentRepository {
    override suspend fun addCategory(category: Content.Category): Unit = con.callToCategoriesCollection {
        if (it.countDocuments(Content.Category::name eq category.name) > 0L) {
            throw ValueAlreadyExistsException()
        }

        it.insertOne(category)
    }

    override suspend fun getCategories(): List<Content.Category> = con.callToCategoriesCollection {
        it.find().toList()
    }

    override suspend fun deleteCategory(name: String): Unit = con.callToCategoriesCollection {
        val count = it.deleteOne(Content.Category::name eq name).deletedCount
        if (count == 0L) throw NotFoundException()

    }

    override suspend fun addMeal(meal: Content.Meal): Unit = con.callToMealsCollection {
        it.insertOne(meal)
    }

    override suspend fun getMeals(): List<Content.Meal> = con.callToMealsCollection {
        it.find().toList()
    }

    override suspend fun getMeal(id: Id<Content.Meal>): Content.Meal = con.callToMealsCollection {
        it.find(Content.Meal::id eq id).first() ?: throw NotFoundException()
    }

    override suspend fun updateMeal(meal: Content.Meal): Unit = con.callToMealsCollection {
        val matched = it.updateOne(Content.Meal::id eq meal.id, meal).matchedCount
        if (matched == 0L) throw NotFoundException()

    }

    override suspend fun deleteMeal(id: Id<Content.Meal>): Unit = con.callToMealsCollection {
        val count = it.deleteOne(Content.Meal::id eq id).deletedCount
        if (count == 0L) throw NotFoundException()
    }


    override suspend fun addReport(report: Content.Report): Unit = con.callToReportsCollection {
        it.insertOne(report)
    }

    override suspend fun getReports(): List<Content.Report> = con.callToReportsCollection {
        it.find().toList()
    }

    override suspend fun getReport(id: Id<Content.Report>): Content.Report = con.callToReportsCollection {
        it.find(Content.Report::id eq id).first() ?: throw NotFoundException()
    }

    override suspend fun updateReport(report: Content.Report): Unit = con.callToReportsCollection {
        val matched = it.updateOne(Content.Report::id eq report.id, report).matchedCount
        if (matched == 0L) throw NotFoundException()

    }

    override suspend fun deleteReport(id: Id<Content.Report>): Unit = con.callToReportsCollection {
        val count = it.deleteOne(Content.Report::id eq id).deletedCount
        if(count == 0L) throw NotFoundException()

    }


}