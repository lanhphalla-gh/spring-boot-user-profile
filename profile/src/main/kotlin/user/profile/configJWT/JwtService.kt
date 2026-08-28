package user.profile.configJWT

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String
) {

    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(
            jwtSecret.toByteArray()
        )

    private val expirationTime = 1000 * 60 * 60 // 1 hour

    fun generateToken(
        userId: UUID,
        username: String,
        role: String
    ): String {

        return Jwts.builder()
            .subject(username)
            .claim("userId", userId.toString())
            .claim("role", role)
            .issuedAt(Date())
            .expiration(
                Date(System.currentTimeMillis() + expirationTime)
            )
            .signWith(secretKey)
            .compact()
    }

    // Extract username from JWT
    fun extractUsername(token: String): String? {
        return try {
            extractAllClaims(token).subject
        } catch (e: Exception) {
            e.message ?: return null
        }
    }

    // Validate JWT
    fun isTokenValid(
        token: String,
        username: String
    ): Boolean {
        return try {
            val extractedUsername = extractUsername(token)

            extractedUsername == username &&
                    !isTokenExpired(token)

        } catch (e: Exception) {
            false
        }
    }

    // Check expiration
    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token)
            .expiration
            .before(Date())
    }

    // Parse and verify JWT
    private fun extractAllClaims(token: String): Claims {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

}