package user.profile.user.dto

import java.util.UUID

data class UserResponseDTO (
    val id: UUID,
    val username: String,
    val email: String,
    val roleName: String?
)