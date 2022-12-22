package de.felix0351.models.objects

import de.felix0351.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

import java.util.UUID

sealed class Content {



    /**
     *  Used to categorize the meals
     *
     * @property id Unique id of the category
     * @property name Name of the category
     */
    @Serializable
    data class Category(
        @BsonId val id: Id<Category>?,
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
        val elements: List<Selection>
    )

    @Serializable
    data class Selection(
        val name: String,
        val clicked: Boolean
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
        @BsonId val id: Id<Meal>?,
        val category: Category?,
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float?,
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
        val name: String,
        val description: String,
        val price: Float,
        val deposit: Float?,
        val day: String?,
        val selections: List<String>,
        val picture: String?,
    )


    /**
     * An Order

     * @property id Unique id of the order
     * @property code QR-Code of the order
     * @property user id of the user, who is ordering
     * @property meals List of the meals, which were ordered
     * @property orderTime ordering time. Automatically generated by the repository
     *
     */
    @Serializable
    data class Order(
        @BsonId val id: Id<Order>?,
        @Serializable(with = UUIDSerializer::class) val code: UUID,
        val user: String,
        val meals: List<OrderedMeal>,
        val orderTime: Long,
    )


    /**
     * Report / New Information
     *
     * @property id Unique id of the report
     * @property title Title of the report
     * @property description Description of the report
     * @property picture Path to the source of the file
     * @property creationTime Time, when the report was created
     *
     */
    @Serializable
    data class Report(
        @BsonId val id: Id<Report>?,
        val title: String,
        val description: String,
        val picture: String,
        val creationTime: Long
    )


}
