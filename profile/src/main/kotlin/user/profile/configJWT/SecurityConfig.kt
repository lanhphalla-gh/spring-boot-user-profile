package user.profile.configJWT

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    /**
     * Password encoder used to hash user passwords.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Configure Spring Security.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http

            // Enable CORS configuration from CorsConfig.
            // Required because Vue frontend and Spring Boot backend
            // are running on different domains.
            .cors {  }

            // Disable CSRF because authentication uses JWT
            // stored in an HttpOnly cookie and the application
            // is configured as a stateless API.
            .csrf { it.disable() }

            // Do not create or use server-side HTTP sessions.
            // Each request is authenticated using the JWT cookie.
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Configure which endpoints require authentication.
            .authorizeHttpRequests {
                it

                    // Allow browser CORS preflight requests.
                    .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                    ).permitAll()

                    // Public endpoints.
                    // These endpoints can be accessed without
                    // authentication or a JWT.
                    .requestMatchers(
                        "/",
                        "/api/auth/login",
                        "/api/contact-admin"
                    ).permitAll()

                    // User management requires authentication.
                    .requestMatchers("/api/user/**").authenticated()

                    // Role management requires authentication.
                    .requestMatchers("/api/role/**").authenticated()

                    // Permission management requires authentication.
                    .requestMatchers("/api/permission/**").authenticated()

                    // Role-permission management requires authentication.
                    .requestMatchers("/api/role-permission/**").authenticated()

                    // Everything else requires authentication.
                    .anyRequest().authenticated()
            }

            // Run the JWT authentication filter before
            // Spring Security's username/password authentication filter.
            .addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}