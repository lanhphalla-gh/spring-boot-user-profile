package user.profile.authentication.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService : JwtService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        // No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // Remove "Bearer " and get the token
        val token = authHeader.substring(7)

        try {
            // Get username from JWT
            val userName = jwtService.extractUsername(token)

            // Check token is valid
            if (userName != null && jwtService.isTokenValid(token, userName)) {
                val authentication = UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    emptyList()
                )
                    SecurityContextHolder.getContext().authentication = authentication
                }
        } catch (_: Exception) {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}