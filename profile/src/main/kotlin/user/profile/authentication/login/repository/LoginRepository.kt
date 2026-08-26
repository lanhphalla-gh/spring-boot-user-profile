package user.profile.authentication.login.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import user.profile.user.User
import java.util.UUID
@Repository
interface LoginRepository: JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?
}