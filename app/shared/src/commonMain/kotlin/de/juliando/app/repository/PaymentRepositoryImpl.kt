package de.juliando.app.repository

import de.juliando.app.data.LocalDataStore
import de.juliando.app.data.ServerDataSource
import de.juliando.app.data.StorageKeys
import de.juliando.app.models.errors.HttpStatusException
import de.juliando.app.models.objects.backend.*
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.utils.asDisplayable

/**
 * This repository handles the payment data.
 * It decides whether the data comes from the server or from local storage.
 */

class PaymentRepositoryImpl(
    private val server: ServerDataSource = ServerDataSource(),
) : PaymentRepository {

    // Save the current shopping cart as an mutable list here
    private val _shoppingCart = mutableListOf<Content.OrderedMeal>()
    override val shoppingCart: List<Content.OrderedMeal> = _shoppingCart

    override fun addItemToShoppingCart(meal: Content.OrderedMeal) = _shoppingCart.add(meal)
    override fun removeItemFromShoppingCart(id: String) = _shoppingCart.removeAll { it.id == id }
    override fun clearShoppingCart() = _shoppingCart.clear()


    override suspend fun getCredit(): Float {
        return server.get("/payment/credit")
    }

    override suspend fun getOrders(): List<Content.Order> {
        return try {
            // Try to get the Data from the Server
            val orders = server.getList<Content.Order>("/payment/orders")
            LocalDataStore.storeList(orders, StorageKeys.ORDER.key)
            orders
        } catch (e: Exception) {
            // Catch: get the Data from the local Storage. If nothing is stored return an empty list.
            LocalDataStore.getList(StorageKeys.ORDER.key) ?: emptyList()
        }
    }

    override suspend fun getOrder(id: String): Order {
        return try {
            // Try to get Data from the stored Order list
            val order = LocalDataStore.getOrderFromList(id)
            if (order!=null) order else throw NullPointerException()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order? {
        return server.post("/payment/order", request)
    }

    override suspend fun deleteOrder(id: String): Content.Order? {
        return server.delete("/payment/order", id)
    }

    override suspend fun verifyOrder(request: VerifyOrderRequest): Order {
        return try {
            val order = server.post<VerifyOrderRequest, Content.Order>("/payment/purchase", request)
            if (order!=null) order.asDisplayable() else throw NullPointerException()
        }catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPurchases(): List<Auth.Payment> {
        return try {
            // Try to get the Data from the Server
            val purchases = server.getList<Auth.Payment>("/payment/purchases")
            LocalDataStore.storeList(purchases, StorageKeys.PAYMENT.key)
            purchases
        } catch (e: Exception) {
            // Catch: get the Data from the local Storage. If nothing is stored return an empty list.
            LocalDataStore.getList(StorageKeys.PAYMENT.key) ?: emptyList()
        }
    }

    override suspend fun deletePurchases() {
        server.delete<String>("/payment/purchases")
    }

    override suspend fun addUserCredit(request: AddCreditRequest) {
        server.post<AddCreditRequest, String>("/payment/credit", request)
    }
}