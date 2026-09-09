package user.profile.authentication.register.repository

import org.springframework.data.jpa.repository.JpaRepository
import user.profile.user.UserEntity
import java.util.UUID

interface RegisterRepository: JpaRepository<UserEntity, UUID> {
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}