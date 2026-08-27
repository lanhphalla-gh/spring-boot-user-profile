package user.profile.user

import org.springframework.stereotype.Service
import user.profile.role.RoleRepository
import user.profile.user.dto.CreateUserRequestDTO
import user.profile.user.dto.UpdatePasswordRequestDTO
import user.profile.user.dto.UpdateUserRequestDTO
import java.util.UUID
import org.springframework.security.crypto.password.PasswordEncoder
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.user.dto.ApplyRoleRequest
import user.profile.user.dto.RemoveRoleRequest
import user.profile.user.mapper.toResponse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    // GET all users
    fun getAllUsers(): ResponseMessageDTO {
        val user = userRepository.findAll()
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User get successfully",
            data = user.map { it.toResponse() }
        )

    }

    // GET user by ID
    fun getUserById(id: UUID): ResponseMessageDTO {

        val user = userRepository.findById(id)
            .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User not found",
            )
        }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User get successfully",
            data = user.toResponse()
        )
    }

    // CREATE user
    fun createUser(request: CreateUserRequestDTO): ResponseMessageDTO {

        // Check username
        if (userRepository.existsByUsername(request.username)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User already exists"
            )
        }

        // Check email
        if (userRepository.existsByEmail(request.email)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Email already exists"
            )
        }

        // Find role
        val role = request.roleId?.let { roleId ->
            roleRepository.findById(roleId)
                .orElse(null)
        }
        if (role != null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role with the same name already exists"
            )
        }

        // Create user
        // Create the object first and assign the properties
        val user = User()
        user.username = request.username
        user.email = request.email
        user.password = passwordEncoder.encode(request.password)
        user.role = role

        // Save user
        val savedUser = userRepository.save(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User created successfully",
            data = savedUser
        )
    }

    // UPDATE user
    fun updateUser(
        id: UUID,
        request: UpdateUserRequestDTO
    ): ResponseMessageDTO {

        val user = userRepository.findById(id)
            .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User does not exists"
            )
        }

        // Update fields
        user.username = request.username
        user.email = request.email

        // Save
        val updatedUser = userRepository.save(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User updated successfully",
            data = updatedUser
        )
    }

    // UPDATE user password
    fun updatePassword(id: UUID, request: UpdatePasswordRequestDTO): ResponseMessageDTO {
        val user = userRepository.findById(id)
            .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User does not exists"
            )
        }

        // 1. Check current password
        if (!passwordEncoder.matches(
                request.currentPassword,
                user.password
            )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Current password is incorrect"
            )
        }

        // 2. Check new password is different from current password
        if (passwordEncoder.matches(
                request.newPassword,
                user.password
            )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "New password must be different from current password"
            )
        }

        // 3. Hash new password
        user.password = passwordEncoder.encode(
            request.newPassword
        )

        // 4. Save
        userRepository.save(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Password updated successfully"
        )
    }

    // DELETE user
    fun deleteUser(id: UUID): ResponseMessageDTO {
        val user = userRepository.findById(id)
            .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User does not exists"
            )
        }

        userRepository.delete(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User deleted successfully"
        )
    }

    // APPLY ROLE TO USER
    fun applyRoleToUser(request: ApplyRoleRequest): ResponseMessageDTO {
        val user = userRepository.findById(request.userId)
            .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User not found with id: $user"
            )
        }
        val role = roleRepository.findById(request.roleId)
            .orElse(null)
        if (role == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role not found with id: $role"
            )
        }

        // Apply role to user
        user.role = role
        userRepository.save(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role applied to user successfully"
        )
    }

    // REMOVE ROLE FROM USER
    fun removeRoleFromUser(request: RemoveRoleRequest): ResponseMessageDTO {
        val user = userRepository.findById(request.userId)
        .orElse(null)
        if (user == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User not found with id: $user"
            )
        }
        user.role = null
        userRepository.save(user)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role removed from user successfully"
        )
    }

}

