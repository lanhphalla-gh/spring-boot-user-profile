package user.profile.authentication.register.repository

import org.springframework.data.jpa.repository.JpaRepository
import user.profile.user.User
import java.util.UUID

interface RegisterRepository: JpaRepository<User, UUID> {
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}