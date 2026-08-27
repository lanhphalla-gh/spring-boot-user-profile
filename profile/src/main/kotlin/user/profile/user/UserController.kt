package user.profile.user

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
import user.profile.user.dto.ApplyRoleRequest
import user.profile.user.dto.CreateUserRequestDTO
import user.profile.user.dto.UpdatePasswordRequestDTO
import user.profile.user.dto.UpdateUserRequestDTO
import java.util.UUID

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    //  Get /api/users/list
    @GetMapping("/list")
    fun getAllUsers(): ResponseEntity<ResponseMessageDTO> {
        val response =  userService.getAllUsers()
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    //  Get /api/user/{id}
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<ResponseMessageDTO> {
        val response = userService.getUserById(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    //  POST /api/user/create
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: CreateUserRequestDTO): ResponseEntity<ResponseMessageDTO> {
        val response = userService.createUser(user)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    //  PUT /api/user/update/{id}
    @PutMapping("/update/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @RequestBody request: UpdateUserRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {
        val response = userService.updateUser(id, request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    //  PUT /api/user/update/password/{id}
    @PutMapping("/update/password/{id}")
    fun updatePassword(
        @PathVariable id: UUID,
        @RequestBody password: UpdatePasswordRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {
        val response = userService.updatePassword(id, password)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }
    //  DELETE /api/user/delete/{id}
    @DeleteMapping("/delete/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<ResponseMessageDTO> {
        val response = userService.deleteUser(id)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }

    // APPLY ROLE TO USER
    @PutMapping("apply-role")
    fun applyRoleToUser(
        @RequestBody request: ApplyRoleRequest
    ): ResponseEntity<ResponseMessageDTO> {
        val response = userService.applyRoleToUser(request)
        return ResponseEntity
            .status(response.code)
            .body(response)
    }
}
