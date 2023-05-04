package de.felix0351.objects

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: String?,
    val tags: List<String>,
    val title: String,
    val description: String,
    val picture: String,
    val creationTime: Long?
)

val exampleReport = Report(
    id = null,
    tags = listOf(),
    title = "Neuer Report",
    description = "Wieder Freitag geschlossen",
    picture = "/var/blabla",
    creationTime = null
)