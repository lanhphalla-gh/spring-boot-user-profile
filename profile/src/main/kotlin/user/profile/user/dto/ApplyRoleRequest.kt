package user.profile.user.dto

import java.util.UUID

data class ApplyRoleRequest(
    val userId: UUID,
    val roleId: UUID
)
