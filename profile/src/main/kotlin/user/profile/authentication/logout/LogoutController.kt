package user.profile.authentication.logout

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

class LogoutController {
    @PostMapping("/logout")
    fun logout(
        response: HttpServletResponse
    ): ResponseEntity<Void> {

        val cookie = ResponseCookie.from("access_token", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build()

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
        )

        return ResponseEntity.noContent().build()
    }
}