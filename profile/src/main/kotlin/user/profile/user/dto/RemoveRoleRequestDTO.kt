package user.profile.user.dto

import java.util.UUID

data class RemoveRoleRequestDTO(
    val userId: UUID,
    val roleId: UUID
)
