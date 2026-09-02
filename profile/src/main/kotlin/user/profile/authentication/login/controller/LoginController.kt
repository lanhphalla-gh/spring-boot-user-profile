package user.profile.authentication.login.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.authentication.login.loginDTO.LoginRequestDTO
import user.profile.authentication.login.loginDTO.LoginResponseDTO
import user.profile.authentication.login.service.LoginService
import java.time.Duration

@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val loginService: LoginService
) {
    // POST /api/auth/login
    @PostMapping("/login")

            /**
             * User Login API
             *
             * Endpoint:
             * POST /api/auth/login
             *
             * Process:
             * 1. Receive username and password from frontend
             * 2. Send login request to LoginService
             * 3. LoginService validates username and password
             * 4. LoginService generates JWT token
             * 5. Store JWT token inside HttpOnly Cookie
             * 6. Return user information, role, and permissions to frontend
             *
             * Note:
             * The JWT token is NOT returned in the JSON response.
             * It is stored securely inside an HttpOnly Cookie.
             */
    fun login(
        @RequestBody loginRequestDTO: LoginRequestDTO,
        response: HttpServletResponse
    ): ResponseEntity<LoginResponseDTO>{

        // Call LoginService to authenticate the user
        // The result contains:
        // - username
        // - role
        // - permissions
        // - JWT token
        val loginResult = loginService.login(loginRequestDTO);

        /**
         * Create JWT HttpOnly Cookie
         *
         * Cookie name: access_token
         *
         * httpOnly(true):
         * JavaScript cannot access the JWT token.
         * This helps protect the token from XSS attacks.
         *
         * secure(true):
         * The cookie is only sent through HTTPS.
         *
         * sameSite("None"):
         * Allows the frontend and backend to communicate
         * when they are hosted on different domains.
         *
         * path("/"):
         * The cookie is available for all API endpoints.
         *
         * maxAge(Duration.ofHours(1)):
         * The cookie expires after 1 hour.
         */
        val cookie = ResponseCookie.from("access_token", loginResult.token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofHours(1))
            .build()

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
        )

        /**
         * Create response data for the frontend.
         *
         * IMPORTANT:
         * We do NOT include the JWT token here.
         *
         * The token is already stored in the HttpOnly Cookie.
         *
         * The frontend only receives:
         * - Login status
         * - Response code
         * - Message
         * - Username
         * - Role
         * - Permissions
         */
        val result = LoginResponseDTO(
            status = "Success",
            code = 200,
            message = "Login successfully",
            username = loginResult.username,
            role = loginResult.role,
            permissions = loginResult.permissions
        )
        return ResponseEntity.ok(result)
    }
}