package user.profile.permission

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PermissionRepository : JpaRepository<Permission, UUID> {

    fun findByName(name: String): Permission?
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: UUID): Boolean
}