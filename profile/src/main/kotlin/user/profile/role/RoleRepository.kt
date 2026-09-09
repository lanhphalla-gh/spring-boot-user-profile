package user.profile.role

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoleRepository: JpaRepository<RoleEntity, UUID> {
    fun findByName(name: String): RoleEntity?
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: UUID): Boolean
}