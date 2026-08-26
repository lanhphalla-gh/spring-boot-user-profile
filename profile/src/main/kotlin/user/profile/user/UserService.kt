package user.profile.user

import org.springframework.stereotype.Service
import user.profile.role.RoleRepository
import user.profile.user.dto.CreateUserRequestDTO
import user.profile.user.dto.UserResponseDTO
import user.profile.user.dto.UpdatePasswordRequestDTO
import user.profile.user.dto.UpdateUserRequestDTO
import java.util.UUID
import org.springframework.security.crypto.password.PasswordEncoder
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.user.mapper.toResponse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    // GET all users
    fun getAllUsers(): List<UserResponseDTO> {
        return userRepository.findAll().map { user ->
            user.toResponse()
        }
    }

    // GET user by ID
    fun getUserById(id: UUID): UserResponseDTO {

        val user = userRepository.findById(id)
            .orElseThrow {
                RuntimeException("User not found with id: $id")
            }
        return user.toResponse()
    }

    // CREATE user
    fun createUser(request: CreateUserRequestDTO): UserResponseDTO {

        // Check username
        if (userRepository.existsByUsername(request.username)) {
            throw RuntimeException(
                "Username already exists: ${request.username}"
            )
        }

        // Check email
        if (userRepository.existsByEmail(request.email)) {
            throw RuntimeException(
                "Email already exists: ${request.email}"
            )
        }

        // Find role
        val role = request.roleId?.let { roleId ->
            roleRepository.findById(roleId)
                .orElseThrow {
                    RuntimeException("Role not found with id: $roleId")
                }
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
        return savedUser.toResponse()
    }

    // UPDATE user
    fun updateUser(
        id: UUID,
        request: UpdateUserRequestDTO
    ): UserResponseDTO {

        val user = userRepository.findById(id)
            .orElseThrow {
                RuntimeException("User not found with id: $id")
            }

        // Find role
        val role = request.roleId?.let { roleId ->
            roleRepository.findById(roleId)
                .orElseThrow {
                    RuntimeException("Role not found with id: $roleId")
                }
        }

        // Update fields
        user.username = request.username
        user.email = request.email
        user.role = role

        // Save
        val updatedUser = userRepository.save(user)
        return updatedUser.toResponse()
    }

    // UPDATE user password
    fun updatePassword(id: UUID, request: UpdatePasswordRequestDTO): ResponseMessageDTO {
        val user = userRepository.findById(id)
            .orElseThrow {
                RuntimeException("User not found with id: $id")
            }

        // 1. Check current password
        if (!passwordEncoder.matches(
                request.currentPassword,
                user.password
            )
        ) {
            throw RuntimeException("Current password is incorrect")
        }

        // 2. Check new password is different
        if (passwordEncoder.matches(
                request.newPassword,
                user.password
            )
        ) {
            throw RuntimeException(
                "New password must be different from current password"
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
    fun deleteUser(id: UUID) {

        if (!userRepository.existsById(id)) {
            throw RuntimeException(
                "User not found with id: $id"
            )
        }
        userRepository.deleteById(id)
    }
}

