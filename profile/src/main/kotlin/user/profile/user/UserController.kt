package user.profile.user

import org.springframework.http.HttpStatus
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
import user.profile.user.dto.CreateUserRequestDTO
import user.profile.user.dto.UpdatePasswordRequestDTO
import user.profile.user.dto.UpdateUserRequestDTO
import user.profile.user.dto.UserResponseDTO
import java.util.UUID

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    //  Get /api/users/list
    @GetMapping("/list")
    fun getAllUsers(): List<UserResponseDTO> {
        return userService.getAllUsers()
    }

    //  Get /api/user/{id}
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): UserResponseDTO {
        return userService.getUserById(id)
    }

    //  POST /api/user/create
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: CreateUserRequestDTO): UserResponseDTO {
        return userService.createUser(user)
    }

    //  PUT /api/user/update/{id}
    @PutMapping("/update/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @RequestBody request: UpdateUserRequestDTO
    ): UserResponseDTO {
        return userService.updateUser(id, request)
    }

    //  PUT /api/user/update/password/{id}
    @PutMapping("/update/password/{id}")
    fun updatePassword(
        @PathVariable id: UUID,
        @RequestBody password: UpdatePasswordRequestDTO
    ): ResponseMessageDTO {
        return userService.updatePassword(id, password)
    }
    //  DELETE /api/user/delete/{id}
    @DeleteMapping("/delete/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }
}
