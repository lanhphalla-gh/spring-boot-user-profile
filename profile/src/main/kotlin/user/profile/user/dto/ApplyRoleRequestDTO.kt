package user.profile.user.dto

import java.util.UUID

data class ApplyRoleRequestDTO(
    val userId: UUID,
    val roleId: UUID
)
