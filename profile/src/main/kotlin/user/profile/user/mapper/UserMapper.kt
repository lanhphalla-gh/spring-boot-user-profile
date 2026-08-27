package user.profile.user.mapper

import user.profile.role.dto.RoleResponse
import user.profile.user.dto.UserResponseDTO

import user.profile.user.User

fun User.toResponse(): UserResponseDTO {
    return UserResponseDTO(
        id = this.id!!,
        username = this.username!!,
        email = this.email!!,
        role = this.role?.let {
            RoleResponse(
                id = it.id,
                name = it.name
            )
        }
    )
}