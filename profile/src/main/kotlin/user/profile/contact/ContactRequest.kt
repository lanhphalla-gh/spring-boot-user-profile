package user.profile.contact

data class ContactRequest(
    val fullName: String,
    val email: String,
    val username: String,
    val message: String
)
