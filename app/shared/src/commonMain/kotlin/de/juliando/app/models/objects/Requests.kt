package de.juliando.app.models.objects

import kotlinx.serialization.Serializable

@Serializable
data class UserDeleteRequest(
    val password: String,
    val username: String
)

@Serializable
data class UserAddRequest(
    val password: String,
    val user: Auth.User
)

@Serializable
data class PasswordChangeRequest(
    val password: String,
    val username: String?,
    val newPassword: String
)

@Serializable
data class NameChangeRequest(
    val password: String,
    val username: String,
    val newName: String
)

@Serializable
data class PermissionChangeRequest(
    val password: String,
    val username: String,
    val newPermissionLevel: Auth.PermissionLevel
)

@Serializable
data class CreateOrderRequest(
    val meals: List<CreateOrderRequestMeal>
)

@Serializable
data class CreateOrderRequestMeal(
    val id: String,
    val selections: List<String>
)

@Serializable
data class VerifyOrderRequest(
    val username: String,
    val orderId: String
)

@Serializable
data class AddCreditRequest(
    val password: String,
    val username: String,
    val credit: Float
)