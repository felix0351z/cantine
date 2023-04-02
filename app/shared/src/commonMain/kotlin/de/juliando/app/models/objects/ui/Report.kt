package de.juliando.app.models.objects.ui


import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: String,
    val title: String,
    val description: String,
    val picture: String?,
    val creationTime: String?
)