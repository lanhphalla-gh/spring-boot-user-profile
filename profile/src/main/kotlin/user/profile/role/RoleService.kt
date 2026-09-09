package user.profile.role
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.role.dto.RoleRequestDTO
import user.profile.role.dto.RoleResponseDTO
import java.util.UUID

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    // GET ALL
    fun getAllRoles(pageable: Pageable): ResponseMessageDTO {
        val roles = roleRepository.findAll(pageable)
            .map { role ->
                RoleResponseDTO(
                    id = role.id,
                    name = role.name
                )
            }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role get successfully",
            data = roles
        )
    }

    // GET BY ID
    fun getRoleById(id: UUID): ResponseMessageDTO {
        val role = roleRepository.findById(id)
            .orElse(null)
        if (role == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Role Not Found",
            )
        }

        val response = roleRepository.findById(id)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Role get successfully",
            data = response
        )
    }

    // CREATE
    fun createRole(request: RoleRequestDTO): ResponseMessageDTO {

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
        val role = RoleEntity()
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
    fun updateRole(id: UUID, request: RoleRequestDTO): ResponseMessageDTO {


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

    fun getRoleCount(): Long {
        return roleRepository.count()
    }
}