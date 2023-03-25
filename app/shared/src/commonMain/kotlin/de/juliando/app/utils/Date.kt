package de.juliando.app.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Format the LocalDateTime in a specific format to a string
 **/
expect fun LocalDateTime.format(format: String): String

/**
* Displays the instant as formatted string in the format "DayName dd.MM.yyyy"
*
**/
fun Instant.asFormattedDescription(): String {
    val dateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.weekDay()} ${dateTime.format("dd.MM.yyyy")} "
}

/*
* Returns the name of the week
*/
//TODO: Use a resource library later
fun LocalDateTime.weekDay(): String = when(this.dayOfWeek.ordinal) {
    0 -> "Montag"
    1 -> "Dienstag"
    2 -> "Mittwoch"
    3 -> "Donnerstag"
    4 -> "Freitag"
    5 -> "Samstag"
    6 -> "Sonntag"
    else -> ""
}