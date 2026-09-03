package user.profile.configJWT

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // Vue frontend
        configuration.allowedOrigins = listOf(
            "http://localhost:5173",
            "https://vuejs-user-profile.onrender.com"
        )

        // Allow HTTP methods
        configuration.allowedMethods = listOf(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        )

        // Allow request headers
        configuration.allowedHeaders = listOf("*")

        // Allow HttpOnly JWT cookie
        configuration.allowCredentials = true

        val source =
            UrlBasedCorsConfigurationSource()

        source.registerCorsConfiguration(
            "/**",
            configuration
        )

        return source

    }
}