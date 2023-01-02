package de.felix0351.dependencies

import de.felix0351.db.MongoDBConnection
import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Collections
import de.felix0351.models.objects.Content
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.time.Instant

class PaymentRepositoryMongoDB(
    private val con: MongoDBConnection
    ) : PaymentRepository {

    private suspend fun getUser(db: CoroutineDatabase, name: String): Auth.User {
        // Get the user
        return db.getCollection<Auth.User>(Collections.USERS.name)
            .findOne(Auth.User::username eq name) ?: throw DatabaseException.NotFoundException()
    }


    private suspend fun updateCredit(db: CoroutineDatabase, username: String, credit: String) {
        // Update the credit amount of the user
        val matched = db.getCollection<Auth.User>(Collections.USERS.name)
            .updateOne(Auth.User::username eq username, set(Auth.User::credit setTo credit)).matchedCount

        if (matched == 0L) throw DatabaseException.NotFoundException()
    }

    override suspend fun getMealsAndCredit(username: String, ids: List<Id<Content.Meal>>): Pair<List<Content.Meal>, String> = con.callWithTransaction {
        val list = it.getCollection<Content.Meal>(Collections.MEALS.name)
            .find(Content.Meal::id `in`  ids).toList()

        // If one of the requested meals wasn't found an exception is needed
        if (list.size != ids.size) throw DatabaseException.NotFoundException()
        // Get the user
        val credit = getUser(it, username).credit

        Pair(list, credit)
    }

    override suspend fun getOrderAndCredit(username: String, id: Id<Content.Order>): Pair<Content.Order, String> = con.callWithTransaction {
        val order = it.getCollection<Content.Order>(Collections.ORDERS.name)
            .findOne(Content.Order::id eq id) ?: throw DatabaseException.NotFoundException()

        // Get the user
        val credit = getUser(it, username).credit

        Pair(order, credit)
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

    override suspend fun verifyOrder(username: String, id: Id<Content.Order>) {
        TODO()
    }

    override suspend fun getOrders(): List<Content.Order> = con.callToOrdersCollection {
        it.find().toList()
    }

    override suspend fun getOrdersFromUser(username: String): List<Content.Order> = con.callToOrdersCollection {
        it.find(Content.Order::user eq username).toList()
    }


    override suspend fun addPayment(payment: Auth.Payment): Unit = con.callToPaymentsCollection {
        it.insertOne(payment)
    }

    override suspend fun getPayments(username: String, range: Instant?): List<Auth.Payment> = con.callToPaymentsCollection {
        // If time is null, it includes all payments
        val time = range ?: Instant.ofEpochSecond(0)

        // Only give the payments which are newer then the time date
        it.find(Auth.Payment::creationTime gte time).toList()
    }

    override suspend fun clearPayments(username: String): Unit = con.callToPaymentsCollection {
        it.deleteMany(Auth.Payment::user eq username)
    }

}