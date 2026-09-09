package user.profile.authentication.login.loginDTO

import user.profile.permission.dto.PermissionResponseDTO
import user.profile.role.dto.RoleResponseDTO

data class LoginResponseDTO(
    val status: String,
    val code: Int,
    val message: String,
    val username: String,
    val role: RoleResponseDTO?,
    val permissions: List<PermissionResponseDTO>
)
