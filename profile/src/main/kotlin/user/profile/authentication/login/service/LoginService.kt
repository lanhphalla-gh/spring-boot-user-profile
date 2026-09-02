package user.profile.authentication.login.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.configJWT.JwtService
import user.profile.authentication.login.loginDTO.LoginRequestDTO
import user.profile.authentication.login.loginDTO.LoginResponseDTO
import user.profile.authentication.login.repository.LoginRepository
import user.profile.permission.dto.PermissionResponse
import user.profile.role.dto.RoleResponse
import user.profile.rolepermission.RolePermissionRepository

@Service
class LoginService (
    private val loginRepository: LoginRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val rolePermissionRepository: RolePermissionRepository
) {
    fun login(
        request: LoginRequestDTO):
            LoginResponseDTO {

        // Find user
        val user = loginRepository.findByUsername(request.username) ?: return LoginResponseDTO(
            status = "Error",
            code = 401,
            message = "Invalid username",
            username = request.username,
            role = null,
            permissions = emptyList()
        )

        // Check password
        if (!passwordEncoder.matches(request.password, user.password)) {
            return LoginResponseDTO(
                status = "Error",
                code = 401,
                message = "Invalid password",
                username = request.username,
                role = null,
                permissions = emptyList()
            )
        }

        // Get role
        val role: RoleResponse? = user.role?.let {
            RoleResponse(
                id = it.id!!,
                name = it.name!!
            )
        }

        // Get permissions from RolePermission
        val permissions: List<PermissionResponse> =
            if (user.role?.id != null) {

                rolePermissionRepository
                    .findByRoleId(user.role!!.id!!)
                    .mapNotNull { rolePermission ->

                        rolePermission.permission?.let { permission ->
                            PermissionResponse(
                                id = permission.id!!,
                                name = permission.name
                            )
                        }
                    }

            } else {
                emptyList()
            }

        // Generate JWT token
        val token = jwtService.generateToken(
            userId = user.id!!,
            username = user.username!!,
            role = "",
        )



        // Login successfully
        return LoginResponseDTO(
            status = "Success",
            code = 200,
            message = "Login successfully",
            username = request.username,
            role = role,
            permissions = permissions,
            token = token
        )
    }
}