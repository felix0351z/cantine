package de.juliando.app.repository

import de.juliando.app.models.objects.*
import de.juliando.app.models.objects.backend.*

/**
 * This repository handles the payment data.
 * It decides whether the data comes from the server or from local storage.
 */

interface PaymentRepository {

    //ShoppingCart

    /**
    * Returns the current shopping cart with all meals
    **/
    val shoppingCart: List<Content.OrderedMeal>

    fun addItemToShoppingCart(meal: Content.OrderedMeal): Boolean
    fun removeItemFromShoppingCart(id: String): Boolean

    fun clearShoppingCart()

    //Credit
    @Throws(Exception::class)
    suspend fun getCredit(): Float

    //Order
    @Throws(Exception::class)
    suspend fun getOrders(): List<Content.Order>
    @Throws(Exception::class)
    suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order?
    @Throws(Exception::class)
    suspend fun deleteOrder(id: String): Content.Order?
    @Throws(Exception::class)
    suspend fun verifyOrder(request: VerifyOrderRequest)

    //Purchase
    @Throws(Exception::class)
    suspend fun getPurchases(): List<Auth.Payment>
    @Throws(Exception::class)
    suspend fun deletePurchases()

    //User
    @Throws(Exception::class)
    suspend fun addUserCredit(request: AddCreditRequest)

}