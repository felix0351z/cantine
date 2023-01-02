package de.felix0351.dependencies

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Content
import org.litote.kmongo.Id
import java.time.Instant
import kotlin.jvm.Throws

interface PaymentRepository {

    //Orders

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun getMealsAndCredit(username: String, ids: List<Id<Content.Meal>>): Pair<List<Content.Meal>, String>

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun getOrderAndCredit(username: String, id: Id<Content.Order>): Pair<Content.Order, String>

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun addOrder(order: Content.Order, username: String, credit: String)

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun cancelOrder(username: String, id: Id<Content.Order>, credit: String)

    suspend fun verifyOrder(username: String, id: Id<Content.Order>)

    suspend fun getOrders(): List<Content.Order>

    suspend fun getOrdersFromUser(username: String): List<Content.Order>

    //Payments

    suspend fun addPayment(payment: Auth.Payment)

    suspend fun getPayments(username: String, range: Instant?): List<Auth.Payment>

    suspend fun clearPayments(username: String)

}