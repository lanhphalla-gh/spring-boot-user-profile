package user.profile.contact.contactDTO

import java.util.UUID

data class ApproveContactRequestDTO(
    val password: String,
    val roleId: UUID
)
