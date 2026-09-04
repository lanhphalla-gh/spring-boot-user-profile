package user.profile.contact.contactDTO

import java.time.LocalDateTime
import java.util.UUID

data class ContactResponseDTO(
    val id: UUID,
    val fullName: String,
    val email: String,
    val username: String,
    val message: String?,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
