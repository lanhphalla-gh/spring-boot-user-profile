package user.profile.permission

import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.permission.dto.PermissionRequest
import user.profile.permission.dto.PermissionResponse
import java.util.UUID

@Service
class PermissionService(
    private val permissionRepository: PermissionRepository
) {
    // GET ALL
    fun getAllPermissions(): ResponseMessageDTO {
        val response = permissionRepository.findAll()
        return ResponseMessageDTO (
            status = "Success",
            code = 200,
            message = "Permissions get successfully",
            data = response
        )
    }

    // GET BY ID
    fun getPermissionById(id: UUID): ResponseMessageDTO {

        val permission = permissionRepository.findById(id)
            .orElse(null)
        if (permission == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Permission Not Found",
            )
        }
        val response = permissionRepository.findById(id)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Permission get successfully",
            data = response
        )
    }

    // CREATE
    fun createPermission(
        request: PermissionRequest
    ): ResponseMessageDTO {

        if (permissionRepository.existsByName(request.name)) {
            throw RuntimeException(
                "Permission already exists: ${request.name}"
            )
        }

        val permission = Permission(
            name = request.name
        )

        val savedPermission =
            permissionRepository.save(permission)

        return ResponseMessageDTO(
            status = "Success",
            code = 201,
            message = "Permission created successfully",
            data = savedPermission
        )
    }

    // UPDATE
    fun updatePermission(
        id: UUID,
        request: PermissionRequest
    ): ResponseMessageDTO {

        // 1. Find existing role
        val permission = permissionRepository.findById(id)
            .orElse(null)
        if (permission == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Permission not found with id: $id"
            )
        }

        // 2. Check if another role already uses this name
        val permissionId = permissionRepository.findByName(request.name)?.id
        if (permissionRepository.existsByNameAndIdNot(request.name, id)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Permission already exists: $permissionId"
            )
        }
        // 3. Update existing role
        permission.name = request.name

        // 4. Save
        val updatedPermission =
            permissionRepository.save(permission)

        return ResponseMessageDTO(
            status = "Success",
            code = 201,
            message = "Permission updated successfully"
        )
    }

    // DELETE
    fun deletePermission(id: UUID): ResponseMessageDTO {

        val permission = permissionRepository.findById(id)
            .orElse(null)
        if (permission == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Permission not found with id: $id"
            )
        }

        val response = permissionRepository.deleteById(id)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Permission deleted successfully",
            data = response
        )
    }

    // ENTITY → RESPONSE
    private fun Permission.toResponse(): PermissionResponse {

        return PermissionResponse(
            id = this.id!!,
            name = this.name
        )
    }

    fun getPermissionCount(): Long {
        return permissionRepository.count()
    }
}