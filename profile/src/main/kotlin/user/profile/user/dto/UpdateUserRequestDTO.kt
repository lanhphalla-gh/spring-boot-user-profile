package user.profile.user.dto

import java.util.UUID

data class UpdateUserRequestDTO (
    val username: String,
    val email: String,
    val roleId: UUID? = null
)