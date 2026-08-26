package user.profile.rolepermission

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RolePermissionRepository :
    JpaRepository<RolePermission, UUID> {
    fun findByRoleId(
        roleId: UUID
    ): List<RolePermission>

    fun existsByRoleIdAndPermissionId(
        roleId: UUID,
        permissionId: UUID
    ): Boolean

    fun findByRoleIdAndPermissionId(
        roleId: UUID,
        permissionId: UUID
    ): RolePermission?
}