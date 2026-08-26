package user.profile.authentication.login.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.authentication.jwt.JwtService
import user.profile.authentication.login.loginDTO.LoginRequestDTO
import user.profile.authentication.login.loginDTO.LoginResponseDTO
import user.profile.authentication.login.repository.LoginRepository

@Service
class LoginService (
    private val loginRepository: LoginRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(request: LoginRequestDTO): LoginResponseDTO {

        // Find user
        val user = loginRepository.findByUsername(request.username) ?: return LoginResponseDTO(
            status = "Error",
            code = 401,
            message = "Invalid username",
            username = request.username,
            role = ""
        )

        // Check password
        if (!passwordEncoder.matches(request.password, user.password)) {
            return LoginResponseDTO(
                status = "Error",
                code = 401,
                message = "Invalid password",
                username = request.username,
                role = ""
            )
        }

        // Get role
        val roleName = user.role?.name?:""

        // Generate JWT token
        val token = jwtService.generateToken(
            userId = user.id!!,
            username = user.username!!,
            role = roleName
        )



        // Login successfully
        return LoginResponseDTO(
            status = "Success",
            code = 200,
            message = "Login successfully",
            username = request.username,
            role = "",
            token = token
        )
    }
}