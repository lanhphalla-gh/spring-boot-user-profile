package user.profile.role
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.role.dto.RoleRequest
import user.profile.role.dto.RoleResponse
import java.util.UUID

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    // GET ALL
    fun getAllRoles(): List<RoleResponse> {
        return roleRepository.findAll().map { role ->
            role.toResponse()
        }
    }

    // GET BY ID
    fun getRoleById(id: UUID): RoleResponse {
        val role = roleRepository.findById(id)
            .orElseThrow {
                RuntimeException("Role not found with id: $id")
            }

        return role.toResponse()
    }

    // CREATE
    fun createRole(request: RoleRequest): ResponseMessageDTO {

        // Check role name
        if (roleRepository.existsByName(request.name)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role already exists: ${request.name}"
            )
        }

        // Create role
        // Create object first and assign the properties
        val role = Role()
        role.name = request.name

        // Save role
        val savedRole = roleRepository.save(role)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role created successfully",
            data = savedRole
        )
    }

    // UPDATE
    fun updateRole(id: UUID, request: RoleRequest): ResponseMessageDTO {


        // 1. Find existing role
        val role = roleRepository.findById(id)
            .orElse(null)
        if (role == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 404,
                message = "Role not found with id: $role"
            )
        }

        // 2. Check if another role already uses this name
        val roleId = roleRepository.findByName(request.name)?.id
        if (roleRepository.existsByNameAndIdNot(request.name, id)) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role already exists by id: $roleId"
            )
        }

        // 3. Update existing role
        role.name = request.name

        // 4. Save
        val updatedRole = roleRepository.save(role)

        return ResponseMessageDTO (
            status = "Success",
            code = 200,
            message = "Role updated successfully",
            data = updatedRole
        )
    }

    // DELETE
    fun deleteRole(id: UUID): ResponseMessageDTO {

        val role = roleRepository.findById(id)
            .orElse(null)
        if (role == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role not found with id: $id"
            )
        }

        val response = roleRepository.deleteById(id)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role deleted successfully",
            data = response
        )
    }

    // ENTITY → RESPONSE
    private fun Role.toResponse(): RoleResponse {

        return RoleResponse(
            id = this.id!!,
            name = this.name
        )
    }
}