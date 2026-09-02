package user.profile.configJWT

import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    fun corsConfigureSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = listOf(
            "http://localhost:5173",
            "https://your-vue-frontend.onrender.com"
        )

        configuration.allowedMethods = listOf(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        )

        configuration.allowedHeaders = listOf("*")

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