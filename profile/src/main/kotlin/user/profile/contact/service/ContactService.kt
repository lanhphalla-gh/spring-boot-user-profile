package user.profile.contact.service

import org.springframework.stereotype.Service
import user.profile.contact.ContactRepository
import user.profile.contact.ContactRequestEntity
import user.profile.contact.contactDTO.ContactRequestDTO
import user.profile.contact.contactDTO.ContactResponseDTO

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val contactEmailService: ContactEmailService,
) {

    fun createContactRequest(request: ContactRequestDTO): ContactResponseDTO {

        // Create entity from request
        val contactRequest = ContactRequestEntity()
        contactRequest.fullName = request.fullName
        contactRequest.email = request.email
        contactRequest.username = request.username
        contactRequest.message = request.message

        // Save request to database
        val savedRequest = contactRepository.save(contactRequest)

        // Send email to admin
        contactEmailService.sendContactRequestEmail(request)

        // Convert entity to response DTO
        return ContactResponseDTO(
            id = savedRequest.id!!,
            fullName = savedRequest.fullName!!,
            email = savedRequest.email!!,
            username = savedRequest.username!!,
            message = savedRequest.message,
            status = savedRequest.status!!,
            createdAt = savedRequest.createdAt,
            updatedAt = savedRequest.updatedAt
        )
    }
}