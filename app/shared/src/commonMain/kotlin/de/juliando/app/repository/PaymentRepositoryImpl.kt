package de.juliando.app.repository

import de.juliando.app.data.LocalDataStoreImpl
import de.juliando.app.data.ServerDataSourceImpl
import de.juliando.app.models.objects.*

class PaymentRepositoryImpl(
    private val server: ServerDataSourceImpl = ServerDataSourceImpl(),
    private val cache: LocalDataStoreImpl = LocalDataStoreImpl()
) : PaymentRepository {

    override suspend fun getCredit(): Float {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): List<Content.Order> {
        TODO("Not yet implemented")
    }

    override suspend fun createOrderRequest(request: CreateOrderRequest): Content.Order {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(id: String): Content.Order {
        TODO("Not yet implemented")
    }

    override suspend fun verifyOrder(request: VerifyOrderRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getPurchases(): List<Auth.Payment> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePurchases() {
        TODO("Not yet implemented")
    }

    override suspend fun addUserCredit(request: AddCreditRequest) {
        TODO("Not yet implemented")
    }
}