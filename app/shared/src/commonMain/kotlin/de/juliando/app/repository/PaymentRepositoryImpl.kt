package de.juliando.app.repository

import de.juliando.app.data.LocalDataStore
import de.juliando.app.data.ServerDataSource
import de.juliando.app.data.StorageKeys
import de.juliando.app.models.errors.HttpStatusException
import de.juliando.app.models.objects.backend.*
import de.juliando.app.models.objects.ui.Order

/**
 * This repository handles the payment data.
 * It decides whether the data comes from the server or from local storage.
 */

class PaymentRepositoryImpl(
    private val server: ServerDataSource = ServerDataSource(),
) : PaymentRepository {

    override suspend fun getCredit(): Float {
        return server.get("/payment/credit")
    }

    override suspend fun getOrders(): List<Order> {
        return try {
            // Try to get the Data from the Server
            val orders = server.getList<Order>("/payment/orders")
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
            throw HttpStatusException.NotFoundException()
        }
    }

    override suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order? {
        return server.post("/payment/order", request)
    }

    override suspend fun deleteOrder(id: String): Content.Order? {
        return server.delete("/payment/order", id)
    }

    override suspend fun verifyOrder(request: VerifyOrderRequest) {
        server.post<VerifyOrderRequest, String>("/payment/purchase", request)
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