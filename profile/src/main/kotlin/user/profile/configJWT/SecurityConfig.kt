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

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http

            // Enable CORS configuration from CorsConfig
            .cors {  }

            // JWT authentication does not use CSRF tokens
            .csrf {it.disable()}

            // Do not create server-side sessions
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it

                    // Allow login and register without authentication
                    .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                    ).permitAll()
                    .requestMatchers(
                        "/",
                        "/api/auth/login",
                        "/api/contact-admin"
                    ).permitAll()
                    .requestMatchers("/api/user/**").authenticated()
                    .requestMatchers("/api/role/**").authenticated()
                    .requestMatchers("/api/permission/**").authenticated()
                    .requestMatchers("/api/role-permission/**").authenticated()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}