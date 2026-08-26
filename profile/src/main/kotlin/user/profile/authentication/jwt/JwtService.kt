package user.profile.authentication.jwt

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
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
}