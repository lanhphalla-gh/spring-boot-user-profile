package user.profile.authentication.register.dto

import java.util.UUID

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val roleId: UUID? = null
)
