package user.profile.rolepermission

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RolePermissionRepository :
    JpaRepository<RolePermissionEntity, UUID> {
    fun existsByRoleIdAndPermissionId(
        roleId: UUID,
        permissionId: UUID
    ): Boolean

    fun findByRoleIdAndPermissionId(
        roleId: UUID,
        permissionId: UUID
    ): RolePermissionEntity?

    fun findByRoleId(
        roleId: UUID
    ): List<RolePermissionEntity>

    fun findByRoleIdIn(
        roleIds: List<UUID>
    ): List<RolePermissionEntity>
}