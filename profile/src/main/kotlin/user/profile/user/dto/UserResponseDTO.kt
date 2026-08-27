package user.profile.user.dto

import user.profile.role.dto.RoleResponse
import java.util.UUID

data class UserResponseDTO (
    val id: UUID,
    val username: String,
    val email: String,
    val role: RoleResponse?
)