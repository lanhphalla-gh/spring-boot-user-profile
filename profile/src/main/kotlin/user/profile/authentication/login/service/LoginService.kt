package user.profile.authentication.login.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.configJWT.JwtService
import user.profile.authentication.login.loginDTO.LoginRequestDTO
import user.profile.authentication.login.loginDTO.LoginResultDTO
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

    /**
     * Authenticate user login.
     *
     * Login Process:
     *
     * 1. Find user by username
     * 2. Validate password
     * 3. Get user's role
     * 4. Get permissions assigned to the role
     * 5. Generate JWT token
     * 6. Return login information and JWT token
     *
     * Note:
     * The JWT token is returned internally to LoginController.
     * LoginController stores the token inside an HttpOnly Cookie.
     */
    fun login(request: LoginRequestDTO): LoginResultDTO {

        /**
         * STEP 1: Find user by username.
         *
         * If the username does not exist,
         * throw an exception and stop the login process.
         */
        val user = loginRepository.findByUsername(request.username)
            ?: throw RuntimeException("Invalid username")

        /**
         * STEP 2: Validate the password.
         *
         * The password from the login request is compared
         * with the encrypted password stored in the database.
         *
         * PasswordEncoder.matches():
         * - request.password = plain password from user
         * - user.password = encrypted password from database
         */
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw RuntimeException("Invalid password")
        }

        /**
         * STEP 3: Get the user's role.
         *
         * Convert the Role entity into RoleResponse DTO.
         *
         * If the user does not have a role,
         * the role will be null.
         */
        val role: RoleResponse? = user.role?.let {
            RoleResponse(
                id = it.id!!,
                name = it.name!!
            )
        }

        /**
         * STEP 4: Get permissions from RolePermission.
         *
         * Relationship:
         *
         * User
         *   ↓
         * Role
         *   ↓
         * RolePermission
         *   ↓
         * Permission
         *
         * Find all RolePermission records using the user's role ID.
         *
         * Then convert each Permission entity
         * into PermissionResponse DTO.
         */
        val permissions: List<PermissionResponse> =
            if (user.role?.id != null) {

                rolePermissionRepository
                    .findByRoleId(user.role!!.id!!)

                    // Convert RolePermission records into PermissionResponse
                    .mapNotNull { rolePermission ->

                        /**
                         * Get the Permission from RolePermission.
                         *
                         * mapNotNull automatically removes null values.
                         */
                        rolePermission.permission?.let { permission ->
                            PermissionResponse(
                                id = permission.id!!,
                                name = permission.name
                            )
                        }
                    }

            } else {

                // If the user does not have a role,
                // return an empty permission list.
                emptyList()
            }

        /**
         * STEP 5: Generate JWT token.
         *
         * The token contains user information that can be used
         * for authentication in future API requests.
         *
         * Current information included:
         * - User ID
         * - Username
         * - Role
         */
        val token = jwtService.generateToken(
            userId = user.id!!,
            username = user.username!!,
            role = "",
        )

        /**
         * STEP 6: Return the login result.
         *
         * LoginResultDTO is used internally between:
         *
         * LoginService
         *      ↓
         * LoginController
         *
         * It contains the JWT token because the controller
         * needs the token to create the HttpOnly Cookie.
         */
        return LoginResultDTO(
            username = user.username!!,
            role = role,
            permissions = permissions,
            token = token
        )
    }
}