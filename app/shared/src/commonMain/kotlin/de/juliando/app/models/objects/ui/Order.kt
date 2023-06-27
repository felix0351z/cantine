package de.juliando.app.models.objects.ui

import de.juliando.app.utils.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val user: String,
    val meals: List<OrderedMeal>,
    val price: String,
    val deposit: String,
    @Serializable(with = InstantSerializer::class) val orderTime: Instant
)
