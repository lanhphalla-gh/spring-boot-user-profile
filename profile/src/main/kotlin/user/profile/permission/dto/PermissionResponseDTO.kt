package user.profile.permission.dto

import java.util.UUID

data class PermissionResponseDTO(
    val id: UUID,
    val name: String
)