package user.profile.authentication.register.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.authentication.register.service.RegisterService
import user.profile.authentication.register.dto.RegisterRequest
import user.profile.messageDTO.ResponseMessageDTO

@RestController
@RequestMapping("/api/auth")
class RegisterController (
    private val registerService: RegisterService
) {
    // POST /api/auth/register
    @PostMapping("/register")
    fun registerUser(
        @RequestBody request: RegisterRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = registerService.register(request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }
}