package user.profile.authentication.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(
            "my-super-secret-key-for-jwt-token-generation-123456".toByteArray()
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
            null
        }
    }

    // Extract user ID from JWT
    fun extractUserId(token: String): UUID? {
        return try {
            val userId = extractAllClaims(token)
                .get("userId", String::class.java)

            UUID.fromString(userId)
        } catch (e: Exception) {
            null
        }
    }

    // Extract role from JWT
    fun extractRole(token: String): String? {
        return try {
            extractAllClaims(token)
                .get("role", String::class.java)
        } catch (e: Exception) {
            null
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