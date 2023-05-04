package de.felix0351.repository

import de.felix0351.db.MongoDBConnection
import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Collections
import de.felix0351.models.objects.Content
import io.ktor.server.plugins.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.time.Instant

class PaymentRepositoryMongoDB(
    private val con: MongoDBConnection
    ) : PaymentRepository {

    private suspend fun updateCredit(db: CoroutineDatabase, username: String, credit: String) {
        // Update the credit amount of the user
        val matched = db.getCollection<Auth.User>(Collections.USERS.name)
            .updateOne(Auth.User::username eq username, set(Auth.User::credit setTo credit)).matchedCount

        if (matched == 0L) throw DatabaseException.NotFoundException()
    }

    override suspend fun getMeals(ids: List<Id<Content.Meal>>): List<Content.Meal> = con.callToMealsCollection {
        val list = it.find(Content.Meal::id `in`  ids).toList()

        // If one of the requested meals wasn't found an exception is needed
        if (list.size != ids.size) throw DatabaseException.NotFoundException()

        list
    }

    override suspend fun getOrder(id: Id<Content.Order>): Content.Order = con.callToOrdersCollection {
        it.findOne(Content.Order::id eq id) ?: throw DatabaseException.NotFoundException()
    }


    override suspend fun addOrder(order: Content.Order, username: String, credit: String): Unit = con.callWithTransaction {
        // Update the credit amount of the user
        updateCredit(it, username, credit)
        // Insert the new order
        it.getCollection<Content.Order>(Collections.ORDERS.name)
            .insertOne(order)
    }

    override suspend fun cancelOrder(username: String, id: Id<Content.Order>, credit: String): Unit = con.callWithTransaction {
        // Delete the order
        val count = it.getCollection<Content.Order>(Collections.ORDERS.name)
            .deleteOne(Content.Order::id eq id).deletedCount
        if (count == 0L) throw DatabaseException.NotFoundException()

        // Update the credit amount of the user
        updateCredit(it, username, credit)
    }

    override suspend fun verifyAndDeleteOrder(id: Id<Content.Order>, payment: Auth.Payment): Unit = con.callWithTransaction {
        // Delete order & insert payment
        val count = it.getCollection<Content.Order>(Collections.ORDERS.name)
            .deleteOne(Content.Order::id eq id).deletedCount
        if (count == 0L) throw NotFoundException()

        it.getCollection<Auth.Payment>(Collections.PAYMENTS.name)
            .insertOne(payment)

    }

    override suspend fun getOrders(): List<Content.Order> = con.callToOrdersCollection {
        it.find().toList()
    }

    override suspend fun getOrdersFromUser(username: String): List<Content.Order> = con.callToOrdersCollection {
        it.find(Content.Order::user eq username).toList()
    }

    //PAYMENTS

    override suspend fun getPayments(username: String, range: Instant?): List<Auth.Payment> = con.callToPaymentsCollection {
        // If time is null, it includes all payments
        val time = range ?: Instant.ofEpochSecond(0)

        // Only give the payments which are newer then the time date
        it.find(Auth.Payment::creationTime gte time, Auth.Payment::user eq username).toList()
    }

    override suspend fun clearPayments(username: String): Unit = con.callToPaymentsCollection {
        it.deleteMany(Auth.Payment::user eq username)
    }

}