package user.profile.contact.contactDTO

data class ContactRequestCountResponseDTO(
    val approved: Long,
    val pending: Long,
    val rejected: Long
)
