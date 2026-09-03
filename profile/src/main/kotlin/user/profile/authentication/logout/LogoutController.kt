package user.profile.authentication.logout

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Marks this class as a REST controller.
// Spring Boot will automatically create a controller bean for it.
@RestController
@RequestMapping("/api/auth")
class LogoutController {

    // Handles POST requests to /logout
    @PostMapping("/logout")
    fun logout(
        response: HttpServletResponse
    ): ResponseEntity<Void> {

        // Create a cookie with the same name as the JWT cookie.
        //
        // Setting the value to "" and maxAge to 0 tells the browser
        // to delete the existing access_token cookie.
        val cookie = ResponseCookie.from("access_token", "")

            // Prevent JavaScript from accessing the JWT cookie.
            // This helps protect the token from XSS attacks.
            .httpOnly(true)

            // Allows the cookie to be sent in cross-site requests.
            // This is commonly needed when Vue frontend and
            // Spring Boot backend are hosted on different origins.
            .secure(true)

            // Allows the cookie to be sent in cross-site requests.
            // This is commonly needed when Vue frontend and
            // Spring Boot backend are hosted on different origins.
            .sameSite("None")

            // Cookie is available to the entire application.
            .path("/")

            // maxAge(0) tells the browser to immediately delete
            // the existing cookie.
            .maxAge(0)

            // Build the final cookie.
            .build()

        // Add the Set-Cookie header to the HTTP response.
        //
        // The browser receives this header and removes the
        // existing access_token cookie.
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
        )

        // Return HTTP 204 No Content.
        //
        // Logout succeeded, and there is no response body needed.
        return ResponseEntity.noContent().build()
    }
}