package user.profile.authentication.login.loginDTO

import user.profile.permission.dto.PermissionResponseDTO
import user.profile.role.dto.RoleResponseDTO

data class LoginResultDTO(
    val username: String,
    val role: RoleResponseDTO?,
    val permissions: List<PermissionResponseDTO>,
    val token: String
)
