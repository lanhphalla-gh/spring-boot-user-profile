package user.profile.authentication.login.loginDTO

import user.profile.permission.dto.PermissionResponse
import user.profile.role.dto.RoleResponse

data class LoginResultDTO(
    val username: String,
    val role: RoleResponse?,
    val permissions: List<PermissionResponse>,
    val token: String
)
