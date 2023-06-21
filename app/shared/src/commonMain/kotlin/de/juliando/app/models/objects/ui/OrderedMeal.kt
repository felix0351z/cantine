package de.juliando.app.models.objects.ui

import kotlinx.serialization.Serializable

@Serializable
data class OrderedMeal (
    val name: String,
    val description: String,
    val price: String,
    val deposit: String,
    val day: String?,
    val selections: List<String>,
    val picture: String?,
)