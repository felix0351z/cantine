package de.felix0351.objects

import de.felix0351.models.objects.Content
import de.felix0351.utils.InstantSerializer
import kotlinx.serialization.Serializable

import java.time.Instant

@Serializable
data class Order(
    val id: String,
    val user: String,
    val meals: List<Content.OrderedMeal>,
    val price: Float,
    val deposit: Float,
    @Serializable(with = InstantSerializer::class) val orderTime: Instant
)

@Serializable
data class VerifyOrderRequest(
    val orderId: String
)