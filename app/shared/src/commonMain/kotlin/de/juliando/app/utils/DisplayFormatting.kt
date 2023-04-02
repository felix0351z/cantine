package de.juliando.app.utils

import de.juliando.app.data.LocalDataStore
import de.juliando.app.models.objects.backend.Content
import de.juliando.app.models.objects.ui.Meal
import de.juliando.app.models.objects.ui.Report

/**
 * Converts the float to an currency value as string
 **/
expect fun toCurrencyString(amount: Float): String

fun toFullImageUrl(pictureId: String?): String? {
    return if (pictureId == null) null
    else "${LocalDataStore.url}/content/image/$pictureId"
}


/**
 * Formatting the [Content.Meal] object into a fully displayable [Meal] data class
 **/
fun asDisplayable(m: List<Content.Meal>): List<Meal> = m.filter { it.id != null }.map {
    Meal(
        id = it.id!!,
        category = it.category,
        name = it.name,
        description = it.description,
        toPay = toCurrencyString(it.price + it.deposit),
        deposit = toCurrencyString(it.deposit),
        day = it.day,
        selections = it.selections,
        picture = toFullImageUrl(it.picture)
    )
}

/**
 * Formatting the [Content.Report] object into a fully displayable [Report] data class
 **/
fun asDisplayableReport(r: List<Content.Report>): List<Report> = r.filter { it.id != null }.map {
    Report(
        id = it.id!!,
        title = it.title,
        description = it.description,
        picture = toFullImageUrl(it.picture),
        creationTime = it.creationTime?.asFormattedDescription()
    )
}
