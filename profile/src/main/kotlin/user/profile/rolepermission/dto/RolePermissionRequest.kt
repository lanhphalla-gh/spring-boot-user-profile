package user.profile.rolepermission.dto

import java.util.UUID

data class RolePermissionRequest (
    val roleId: UUID,
    val permissionId: UUID
)