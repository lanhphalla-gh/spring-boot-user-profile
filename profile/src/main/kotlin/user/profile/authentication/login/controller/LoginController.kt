package user.profile.authentication.login.controller

import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.authentication.login.loginDTO.LoginRequestDTO
import user.profile.authentication.login.loginDTO.LoginResponseDTO
import user.profile.authentication.login.service.LoginService

@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val loginService: LoginService
) {
    // POST /api/auth/login
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDTO: LoginRequestDTO
    ): ResponseEntity<LoginResponseDTO>{
        val response = loginService.login(loginRequestDTO)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }
}