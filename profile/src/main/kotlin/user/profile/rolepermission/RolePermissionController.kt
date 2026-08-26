package user.profile.rolepermission

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.rolepermission.dto.RolePermissionRequest
import java.util.UUID

@RestController
@RequestMapping("/api/role-permission")
class RolePermissionController(
    private val rolePermissionService: RolePermissionService
) {
    // GET /api/role-permissions
    @GetMapping("/list")
    fun getAllRolePermissions(): ResponseEntity<ResponseMessageDTO> {

        val response = rolePermissionService.getAllRolePermissions()
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // POST /api/role-permissions
    @PostMapping("/create")
    fun createRolePermission(
        @RequestBody request: RolePermissionRequest
    ): ResponseEntity<ResponseMessageDTO> {

        val response = rolePermissionService.createRolePermission(request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // Remove ONE permission
    @DeleteMapping("/removepermission")
    fun removePermissionFromRole(
        @RequestBody request: RolePermissionRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = rolePermissionService.removePermissionFromRole(request)

        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // Remove ALL permissions
    @DeleteMapping("/removeallpermission/{roleId}")
    fun removePermissionsFromRole(
        @PathVariable roleId: UUID,
    ): ResponseEntity<ResponseMessageDTO> {
        val response = rolePermissionService.removeAllPermissionsFromRole(roleId)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }
}