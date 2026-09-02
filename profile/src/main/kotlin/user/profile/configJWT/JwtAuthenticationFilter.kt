package user.profile.configJWT

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService : JwtService,
    private val userDetailsService: CustomUserDetailsService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = request.cookies
            ?.firstOrNull() {
                it.name == "access_token"
            }
            ?.value

        if (token != null) {
            try {
                val username = jwtService.extractUsername(token)

                if (
                    username != null &&
                    SecurityContextHolder.getContext().authentication == null
                ) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    if (
                        jwtService.isTokenValid(
                            token,
                            username
                        )
                    ) {
                        val authentication =
                            UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.authorities
                            )

                        authentication.details =
                            WebAuthenticationDetailsSource()
                                .buildDetails(request)

                        SecurityContextHolder
                            .getContext()
                            .authentication = authentication
                    }
                }
            } catch (exception: Exception) {
                logger.error(
                    "JWT authentication failed",
                    exception
                )
            }
        }
        filterChain.doFilter(request, response)
    }
}