package user.profile.user.dto

import java.util.UUID

data class CreateUserRequestDTO (
    val username: String,
    val email: String,
    val password: String,
    val roleId: UUID? = null
)