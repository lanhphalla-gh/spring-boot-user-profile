package user.profile.user.dto

data class UpdatePasswordRequestDTO (
    val currentPassword: String,
    val newPassword: String
)