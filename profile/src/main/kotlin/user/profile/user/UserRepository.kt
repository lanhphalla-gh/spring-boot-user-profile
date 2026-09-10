package user.profile.user

import org.springframework.data.jpa.repository.JpaRepository
import user.profile.user.dto.CreateUserRequestContactDTO
import java.util.UUID

interface UserRepository: JpaRepository<UserEntity, UUID> {
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun save(entity: CreateUserRequestContactDTO): UserEntity
}