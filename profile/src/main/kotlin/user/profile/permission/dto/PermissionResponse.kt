package user.profile.permission.dto

import java.util.UUID

data class PermissionResponse(
    val id: UUID,
    val name: String
)