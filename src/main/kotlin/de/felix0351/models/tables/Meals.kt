package de.felix0351.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

sealed class Meals {

    /**
     *  Used to categorize the meals
     *
     *  @property id Unique id of the category l
     * @property name Name of the category
     *
     */
    object Categories : Table("categories") {

        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", 128)

        override val primaryKey = PrimaryKey(id)
    }

    /**
     *  Stores all meals
     *
     *  @property id Unique id of the meal. Automatically generated.
     * @property name Name of the meal
     * @property description A short description about the meal
     * @property price Price of the meal
     * @property day Day, if the meal is available. Null if the meal is available continuously
     * @property selections Selections to the meal, which will be stored in JSON Format.
     * Templates can be stored in the SelectionGroup Table
     * @property category categoryID(Category Table), can be null if the meal has no category.
     * If the category to the meal will be deleted, then it will be null.
     *
     */
    object Meals : Table("meals") {

        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", 128)
        val description: Column<String> = varchar("description", 128)
        val price: Column<Float> = float("price")
        val day: Column<String?> = varchar("day", 128).nullable()
        val selections: Column<String?> = varchar("selections", 512).nullable()
        val category: Column<Int?> =
            integer("categoryID")
                .references(Categories.id, onDelete = ReferenceOption.SET_NULL)
                .nullable()

        override val primaryKey = PrimaryKey(id)
    }

    /**
     * Stores all current orders from the system
     *
     * @property userID id of the user (User Table), who is ordering
     * @property mealID id of the meal (Meal Table), which was ordered
     * @property time ordering time. Automatically generated
     *  If the user or the meal will be deleted, orders referred to it, will be too.
     *
     */
    object Orders : Table("orders") {

        val userID: Column<Int> =
            integer("userID")
                .references(Users.id, onDelete = ReferenceOption.CASCADE)
        val mealID: Column<Int> =
            integer("mealID")
                .references(Meals.id, onDelete = ReferenceOption.CASCADE)

        val time: Column<LocalDateTime> = datetime("order_time").autoIncrement()


        override val primaryKey = PrimaryKey(userID)


    }


    /**
     * Table to store selection group templates
     *
     * @property id Unique id of the group. Automatically generated
     * @property name Name of the group
     * @property json Data and Elements in the Group (stored in JSON Format)
     *
     */
    object SelectionGroup : Table("selection_group") {
        val id: Column<Int> = integer("id").autoIncrement()
        val name: Column<String> = varchar("name", 128)
        val json: Column<String> = varchar("json", 512)

        override val primaryKey = PrimaryKey(id)

    }


}
