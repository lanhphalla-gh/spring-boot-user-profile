package user.profile.rolepermission
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.permission.PermissionRepository
import user.profile.permission.dto.PermissionResponse
import user.profile.role.RoleRepository
import user.profile.role.dto.RoleResponse
import user.profile.rolepermission.dto.RolePermissionRequest
import user.profile.rolepermission.dto.RolePermissionResponse
import java.util.UUID

@Service
class RolePermissionService(
    private val rolePermissionRepository: RolePermissionRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {
    // GET ALL
    fun getAllRolePermissions(): ResponseMessageDTO{

        val rolePermissions = rolePermissionRepository.findAll()
        val response = rolePermissions
            .filter {
                it.role != null && it.permission != null
            }
            .groupBy {
                it.role!!.id!!
            }
            .map { (_, items) ->
                val role = items.first().role!!
                RolePermissionResponse(
                    role = RoleResponse(
                        id = role.id!!,
                        name = role.name!!,
                    ),
                    permissions = items.map { item ->
                        val permission = item.permission!!
                        PermissionResponse(
                            id = permission.id!!,
                            name = permission.name,
                        )
                    }
                )
            }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role Permission retrieved successfully",
            data = response
        )
    }

    // CREATE
    fun createRolePermission(request: RolePermissionRequest): ResponseMessageDTO {

        // 1. Check duplicate
        if (
            rolePermissionRepository.existsByRoleIdAndPermissionId(
                request.roleId,
                request.permissionId
            )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role Permission already exists",
            )
        }

        // 2. Create RolePermission
        val rolePermission = RolePermission()
        val checkRole = roleRepository.findById(request.roleId).orElse(null)
        val permission = permissionRepository.findById(request.permissionId).orElse(null)
        rolePermission.role = checkRole
        rolePermission.permission = permission

        // 5. Save
        val response = rolePermissionRepository.save(rolePermission)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role Permission added successfully",
            data = response
        )
    }

    // DELETE ONE PERMISSION FROM ROLE
    fun removePermissionFromRole(
        request: RolePermissionRequest
    ): ResponseMessageDTO {

        // 1. Find the relationship
        val rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(
            request.roleId,
            request.permissionId
        )

        if (rolePermission == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "This permission does not assigned to the role",
            )
        }

        // 2. Delete ONLY this relationship
        rolePermissionRepository.delete(rolePermission)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Permission removed from role successfully",
        )
    }

    // DELETE ALL PERMISSIONS FROM ROLE
    fun removeAllPermissionsFromRole(
        roleId: UUID,
    ): ResponseMessageDTO {

        // 1. Check Role
        val role = roleRepository.findById(roleId)
        .orElse(null)
        if (role == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role Not Found",
            )
        }

        // 2. Find all RolePermission records
        val rolePermission = rolePermissionRepository.findByRoleId(roleId)
        if (rolePermission.isEmpty()) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "This role has no permission",
            )
        }

        // 3. Delete All RolePermission records
        rolePermissionRepository.deleteAll(rolePermission)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "All permissions removed from role successfully",
        )
    }

    fun getRolePermissionCount(): Long {
        return rolePermissionRepository.count()
    }

}