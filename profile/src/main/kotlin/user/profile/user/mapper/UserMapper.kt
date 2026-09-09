package user.profile.user.mapper

import user.profile.role.dto.RoleResponseDTO
import user.profile.user.dto.UserResponseDTO

import user.profile.user.UserEntity

fun UserEntity.toResponse(): UserResponseDTO {
    return UserResponseDTO(
        id = this.id!!,
        username = this.username!!,
        email = this.email!!,
        role = this.role?.name
    )
}