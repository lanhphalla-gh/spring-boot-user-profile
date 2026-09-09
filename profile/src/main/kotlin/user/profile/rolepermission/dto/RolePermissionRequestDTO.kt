package user.profile.rolepermission.dto

import java.util.UUID

data class RolePermissionRequestDTO (
    val roleId: UUID,
    val permissionId: UUID
)