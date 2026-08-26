package user.profile.authentication.register.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.authentication.register.dto.RegisterRequest
import user.profile.authentication.register.repository.RegisterRepository
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.role.Role
import user.profile.role.RoleRepository
import user.profile.user.User

@Service
class RegisterService (
    private val registerRepository : RegisterRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun  register(request: RegisterRequest): ResponseMessageDTO {
        // Check username
        if (registerRepository.existsByUsername(request.username)) {
            return ResponseMessageDTO (
                status = "Error",
                code = 400,
                message = "Username is already exists!"
            )

        }

        // Check email
        if (registerRepository.existsByEmail(request.email)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Email is already exists!"
            )
        }

        // Find role
        val role = request.roleId?.let { roleRepository.findById(it) }

        // Create user
        val user = User();
        user.username = request.username
        user.email = request.email

        // IMPORTANT: hash password before saving
        user.password = passwordEncoder.encode(request.password)
        user.role = role as Role?

        // Save
        registerRepository.save(user)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User registered successfully!"
        )
    }
}