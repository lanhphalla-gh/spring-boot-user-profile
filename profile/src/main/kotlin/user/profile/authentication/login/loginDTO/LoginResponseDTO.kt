package user.profile.authentication.login.loginDTO

import user.profile.permission.dto.PermissionResponse
import user.profile.role.dto.RoleResponse

data class LoginResponseDTO(
    val status: String,
    val code: Int,
    val message: String,
    val username: String,
    val role: RoleResponse?,
    val permissions: List<PermissionResponse>,
    val token: String? = null
)
