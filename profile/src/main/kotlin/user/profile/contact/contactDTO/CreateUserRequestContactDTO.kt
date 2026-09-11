package user.profile.contact.contactDTO

import java.util.UUID

data class CreateUserRequestContactDTO(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val roleId: UUID? = null
)
