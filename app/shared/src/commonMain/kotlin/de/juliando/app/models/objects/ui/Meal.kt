package de.juliando.app.models.objects.ui

import de.juliando.app.models.objects.backend.Content
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val id: String,
    val category: String?,
    val name: String,
    val description: String,
    val toPay: String,
    val deposit: String,
    val day: String?,
    val selections: List<Content.SelectionGroup>,
    val picture: String?,
) {



    fun matchQuery(query: String): Boolean {
        val fields = listOf(name, category, description, day)
        return fields.filterNotNull().any {
            it.contains(query, ignoreCase = true)
        }
    }

}