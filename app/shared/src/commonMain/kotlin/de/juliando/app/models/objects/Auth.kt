package de.juliando.app.models.objects

import kotlinx.serialization.Serializable

sealed class Auth {

    @Serializable
    data class PublicUser(
        val username: String,
        val name: String,
        val permissionLevel: PermissionLevel,
        val credit: Float,
        val password: String? = null
    )

    @Serializable
    data class Payment(
        val user: String,
        val meals: List<String>,
        val price: Float,
        val creationTime: String
    )

    enum class PermissionLevel(val int: Int) {
        ADMIN(2),
        WORKER(1),
        USER(0)
    }
}
