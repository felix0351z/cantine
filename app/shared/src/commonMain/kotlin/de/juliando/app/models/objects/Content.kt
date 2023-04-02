package de.juliando.app.models.objects

import de.juliando.app.utils.InstantSerializer
import de.juliando.app.utils.PictureSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

sealed class Content {

    @Serializable
    data class Category(
        val name: String
    )

    @Serializable
    data class SelectionGroup(
        val name: String,
        val elements: List<Selection>
    )

    @Serializable
    data class Selection(
        val name: String,
        val price: Float
    )

    @Serializable
    data class Meal(
        val id: String?,
        val category: String?,
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float,
        val day: String?,
        val selections: List<SelectionGroup>,
        @Serializable(with = PictureSerializer::class) val picture: String?,
    )

    @Serializable
    data class OrderedMeal(
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float,
        val day: String?,
        val selections: List<String>,
        @Serializable(with = PictureSerializer::class) val picture: String?,
    )

    @Serializable
    data class Order(
        val id: String,
        val user: String,
        val meals: List<OrderedMeal>,
        val price: Float,
        val deposit: Float,
        @Serializable(with = InstantSerializer::class) val orderTime: Instant
    )

    @Serializable
    data class Report(
        val id: String?,
        val title: String,
        val description: String,
        @Serializable(with = PictureSerializer::class) val picture: String?,
        @Serializable(with = InstantSerializer::class) val creationTime: Instant?
    )
}