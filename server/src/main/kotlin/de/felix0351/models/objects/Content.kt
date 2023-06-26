package de.felix0351.models.objects

import de.felix0351.utils.CustomIDSerializer
import de.felix0351.utils.InstantSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.Instant

sealed class Content {



    /**
     *  Used to categorize the meals
     *
     * @property name Name of the category
     */
    @Serializable
    data class Category(
        val name: String
    )


    /**
     * Selection Group
     *
     * @property name Name of the group
     * @property elements Data and Elements in the Group (stored in JSON Format)
     *
     */
    @Serializable
    data class SelectionGroup(
        val name: String,
        val elements: List<Selection>,
    )

    @Serializable
    data class Selection(
        val name: String,
        val price: Float
    )


    /**
     *  Meal Object
     *
     * @property id Unique id of the meal
     * @property category Category to the meal can be null if the meal has no category.
     * @property name Name of the meal
     * @property description A short description about the meal
     * @property price Price of the meal
     * @property deposit Deposit, if present
     * @property day Day, if the meal is available. Null if the meal is available continuously
     * @property selections Selections to the meal, which will be stored in JSON Format.
     * Templates can be stored in the SelectionGroup Table
     * @property picture Storage address to the picture of the meal if there is one
     *
     */

    @Serializable
    data class Meal(
        @BsonId @Serializable(with = CustomIDSerializer::class) val id: Id<Meal>,
        val category: String?,
        val tags: List<String>,
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float,
        val day: String?,
        val selections: List<SelectionGroup>,
        val picture: String?,
    )

    /**
     *  Ordered Meal Object
     *
     * @property name Name of the meal
     * @property description A short description about the meal
     * @property price Price of the meal
     * @property deposit Deposit, if present
     * @property day Day, if the meal is available. Null if the meal is available continuously
     * @property selections Selections to the meal, which the user has chosen.
     * Templates can be stored in the SelectionGroup Table
     * @property picture Storage address to the picture of the meal if there is one
     *
     */

    @Serializable
    data class OrderedMeal(
        @BsonId @Serializable(with = CustomIDSerializer::class) val id: Id<Meal>,
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float,
        val day: String?,
        val selections: List<String>,
        val picture: String?,
    )


    /**
     * An Order

     * @property id Unique id of the order
     * @property user username of the user who created the order
     * @property meals List of the meals, which were ordered
     * @property price Price of all meals together
     * @property deposit Deposit of all meals together
     * @property orderTime ordering time. Automatically generated by the repository
     *
     */
    @Serializable
    data class Order(
        @BsonId @Serializable(with = CustomIDSerializer::class) val id: Id<Order>,
        val user: String,
        val meals: List<OrderedMeal>,
        val price: Float,
        val deposit: Float,
        @Serializable(with = InstantSerializer::class) val orderTime: Instant
    )


    /**
     * Report / New Information
     *
     * @property id Unique id of the report
     * @property title Title of the report
     * @property description Description of the report
     * @property picture Path to the source of the file, will be null if none image is provided
     * @property creationTime Time, when the report was created
     *
     */
    @Serializable
    data class Report(
        @BsonId @Serializable(with = CustomIDSerializer::class) val id: Id<Report>,
        val title: String,
        val tags: List<String>,
        val description: String,
        val picture: String?,
        @Serializable(with = InstantSerializer::class) val creationTime: Instant
    )


}
