package user.profile.authentication.login.loginDTO

data class LoginResponseDTO(
    val status: String,
    val code: Int,
    val message: String,
    val username: String,
    val role: String,
    val token: String? = null
)
