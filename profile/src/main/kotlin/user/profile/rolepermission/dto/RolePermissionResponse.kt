package user.profile.rolepermission.dto

import user.profile.permission.dto.PermissionResponse
import user.profile.role.dto.RoleResponse

class RolePermissionResponse (
    val role: RoleResponse,
    val permissions: List<PermissionResponse>
)
