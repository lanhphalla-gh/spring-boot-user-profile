package user.profile.permission

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
import user.profile.permission.dto.PermissionRequest
import user.profile.permission.dto.PermissionResponse
import java.util.UUID

@RestController
@RequestMapping("/api/permission")
class PermissionController(val permissionService: PermissionService) {
    // GET /api/permissions
    @GetMapping("list")
    fun getAllPermissions(): ResponseEntity<ResponseMessageDTO> {
        val response = permissionService.getAllPermissions()
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // GET /api/permissions/{id}
    @GetMapping("/{id}")
    fun getPermissionById(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {
        val response = permissionService.getPermissionById(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // POST /api/permissions
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPermission(
        @RequestBody request: PermissionRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = permissionService.createPermission(request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // PUT /api/permissions/{id}
    @PutMapping("/update/{id}")
    fun updatePermission(
        @PathVariable id: UUID,
        @RequestBody request: PermissionRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = permissionService.updatePermission(
            id,
            request
        )
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // DELETE /api/permissions/{id}
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePermission(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {
        val response = permissionService.deletePermission(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    @GetMapping("/count")
    fun getPermissionCount(): ResponseEntity<Long> {

        val count = permissionService.getPermissionCount()

        return ResponseEntity.ok(count)
    }
}