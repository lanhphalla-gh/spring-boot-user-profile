package user.profile.authentication.login.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import user.profile.user.UserEntity
import java.util.UUID
@Repository
interface LoginRepository: JpaRepository<UserEntity, UUID> {
    fun findByUsername(username: String): UserEntity?
}