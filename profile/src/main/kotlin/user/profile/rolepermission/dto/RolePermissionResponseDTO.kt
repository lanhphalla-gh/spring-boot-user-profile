package user.profile.rolepermission.dto

import user.profile.permission.dto.PermissionResponseDTO
import user.profile.role.dto.RoleResponseDTO

class RolePermissionResponseDTO (
    val role: RoleResponseDTO,
    val permissions: List<PermissionResponseDTO>
)
