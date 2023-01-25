package de.felix0351.objects

import kotlinx.serialization.Serializable

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
    val picture: String?,
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
data class CreateOrderRequest(
    val meals: List<CreateOrderRequestMeal>
)

@Serializable
data class CreateOrderRequestMeal(
    val id: String,
    val selections: List<String>
)



val exampleMeal = Meal(
    id = null,
    category = "dfgfd",
    name = "Pommes",
    description = "Frittierte Kartoffeln in Stangenform lol",
    price = 2.2F,
    deposit = 0F,
    day = null,
    selections = listOf(
        SelectionGroup(
            name = "Soße",
            elements = listOf(
                Selection("Ketchup", 0.1F),
                Selection("Rahmsoße", 0.2F)
            )
        )
    ),
    picture = "/var/blabla"
)