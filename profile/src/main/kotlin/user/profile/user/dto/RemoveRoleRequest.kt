package user.profile.user.dto

import java.util.UUID

data class RemoveRoleRequest(
    val userId: UUID,
    val roleId: UUID
)
