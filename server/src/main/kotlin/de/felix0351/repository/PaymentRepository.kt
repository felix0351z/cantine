package de.felix0351.repository

import de.felix0351.models.errors.DatabaseException
import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Content
import org.litote.kmongo.Id
import java.time.Instant
import kotlin.jvm.Throws

interface PaymentRepository {

    //Orders

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun getMeals(ids: List<Id<Content.Meal>>): List<Content.Meal>

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun getOrder(id: Id<Content.Order>): Content.Order

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun addOrder(order: Content.Order, username: String, credit: String)

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun cancelOrder(username: String, id: Id<Content.Order>, credit: String)

    @Throws(DatabaseException.NotFoundException::class)
    suspend fun verifyAndDeleteOrder(id: Id<Content.Order>, payment: Auth.Payment)

    suspend fun getOrders(): List<Content.Order>

    suspend fun getOrdersFromUser(username: String): List<Content.Order>

    //Payments

    suspend fun getPayments(username: String, range: Instant?): List<Auth.Payment>

    suspend fun clearPayments(username: String)

}