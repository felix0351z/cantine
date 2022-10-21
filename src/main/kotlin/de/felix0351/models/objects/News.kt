package de.felix0351.models.objects

import de.felix0351.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class News(
    val id: Int?,
    val title: String,
    val description: String,
    val picture: String,
    @Serializable(with = LocalDateTimeSerializer::class) val time: LocalDateTime
)
