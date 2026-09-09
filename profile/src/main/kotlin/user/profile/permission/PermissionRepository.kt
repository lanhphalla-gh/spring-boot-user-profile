package user.profile.permission

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PermissionRepository : JpaRepository<PermissionEntity, UUID> {

    fun findByName(name: String): PermissionEntity?
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: UUID): Boolean
}