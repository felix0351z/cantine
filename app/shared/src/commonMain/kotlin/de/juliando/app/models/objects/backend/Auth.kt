package de.juliando.app.models.objects.backend

import de.juliando.app.utils.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

sealed class Auth {

    @Serializable
    data class User(
        val username: String,
        val name: String,
        val permissionLevel: PermissionLevel,
        val credit: Float,
        val password: String?
    )

    @Serializable
    data class Payment(
        val user: String,
        val meals: List<String>,
        val price: Float,
        @Serializable(with = InstantSerializer::class) val creationTime: Instant
    )

    enum class PermissionLevel(val int: Int) {
        ADMIN(2),
        WORKER(1),
        USER(0)
    }
}
