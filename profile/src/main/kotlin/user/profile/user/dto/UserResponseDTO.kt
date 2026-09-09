package user.profile.user.dto

import user.profile.role.dto.RoleResponseDTO
import java.util.UUID

data class UserResponseDTO (
    val id: UUID? = null,
    val username: String? = null,
    val email: String? = null,
    val role: String? = null,
)