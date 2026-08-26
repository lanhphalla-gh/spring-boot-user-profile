package user.profile.authentication.jwt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http

            .csrf {it.disable()}
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/login").permitAll()
                    .requestMatchers("/api/users/**").authenticated()
                    .requestMatchers("/api/roles/**").authenticated()
                    .requestMatchers("/api/permissions/**").authenticated()
                    .requestMatchers("/api/role-permission/**").authenticated()
//                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }
}