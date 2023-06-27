package de.juliando.app.utils

import de.juliando.app.data.LocalDataStore
import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.models.objects.ui.Order
import de.juliando.app.models.objects.ui.OrderedMeal
import de.juliando.app.models.objects.ui.Report
import kotlin.jvm.JvmName

/**
 * Converts the float to an currency value as string
 **/
expect fun toCurrencyString(amount: Float): String

fun toFullImageUrl(pictureId: String?): String? {
    return if (pictureId == null) null
    else "${LocalDataStore.getURL()}/content/image/$pictureId"
}


/**
 * Formatting the [Content.Meal] object into a fully displayable [Meal] data class
 **/

@JvmName("asDisplayableMealList")
fun List<Content.Meal>.asDisplayable(): List<Meal> =
    filter { it.id != null }
    .map { it.asDisplayable() }

@JvmName("asDisplayableMeal")
fun Content.Meal.asDisplayable() = Meal(
    id = id!!,
    category = category,
    tags = tags,
    name = name,
    description = description,
    toPay = toCurrencyString(price + deposit),
    deposit = toCurrencyString(deposit),
    day = day,
    selections = selections,
    picture = toFullImageUrl(picture)
)

/**
 * Formatting the [Content.Report] object into a fully displayable [Report] data class
 **/
@JvmName("asDisplayableReportList")
fun List<Content.Report>.asDisplayable(): List<Report> =
    filter { it.id != null }
    .map { it.asDisplayable() }

@JvmName("asDisplayableReport")
fun Content.Report.asDisplayable() = Report(
    id = id!!,
    title = title,
    tags = tags,
    description = description,
    picture = toFullImageUrl(picture),
    creationTime = creationTime?.asFormattedDescription()
)

fun List<Content.OrderedMeal>.asDisplayable(): List<OrderedMeal> =
    filter { it.id != null }
    .map { it.asDisplayable() }

@JvmName("asDisplayableOrderedMeal")
fun Content.OrderedMeal.asDisplayable() = OrderedMeal(
    id = id!!,
    name = name,
    description = description,
    price = toCurrencyString(price + deposit),
    deposit = toCurrencyString(deposit),
    day = day,
    selections = selections,
    picture = toFullImageUrl(picture)
)

fun Content.Meal.toOrderedMeal(selections: List<String>) = Content.OrderedMeal(
    id = id,
    name = name,
    description = description,
    price = price,
    deposit = deposit,
    day = day,
    selections = selections,
    picture = picture
)
@JvmName("asDisplayableOrderList")
fun List<Content.Order>.asDisplayable() =
    map { it.asDisplayable() }

@JvmName("asDisplayableOrder")
fun Content.Order.asDisplayable() = Order(
    id = id,
    user = user,
    meals = meals.asDisplayable(),
    price = toCurrencyString(price+deposit),
    deposit = toCurrencyString(deposit),
    orderTime = orderTime
)