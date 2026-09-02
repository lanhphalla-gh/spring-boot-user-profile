package user.profile.role

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.role.dto.RoleRequest
import user.profile.role.dto.RoleResponse
import java.util.UUID

@RestController
@RequestMapping("/api/role")
class RoleController(
    private val roleService: RoleService
) {
    //GET /api/roles
    @GetMapping("/list")
    fun getAllRoles(): ResponseEntity<ResponseMessageDTO> {
        val response = roleService.getAllRoles()
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // GET /api/roles/{id}
    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: UUID): ResponseEntity<ResponseMessageDTO> {
        val response = roleService.getRoleById(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // POST /api/roles
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createRole(
        @RequestBody request: RoleRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = roleService.createRole(request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // PUT /api/roles/{id}
    @PutMapping("/update/{id}")
    fun updateRole(
        @PathVariable id: UUID,
        @RequestBody request: RoleRequest
    ): ResponseEntity<ResponseMessageDTO>{
        val response = roleService.updateRole(id, request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // DELETE /api/roles/{id}
    @DeleteMapping("/delete/{id}")
    fun deleteRole(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {
        val response = roleService.deleteRole(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    @GetMapping("/count")
    fun getRoleCount(): ResponseEntity<Long> {

        val count = roleService.getRoleCount()

        return ResponseEntity.ok(count)
    }
}