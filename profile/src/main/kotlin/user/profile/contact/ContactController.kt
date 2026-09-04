package user.profile.contact

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.contact.contactDTO.ContactRequestDTO
import user.profile.contact.contactDTO.ContactResponseDTO
import user.profile.contact.service.ContactService

@RestController
@RequestMapping("/api/contact-admin")
class ContactController(
    private val contactService: ContactService
) {
    private val logger =
        LoggerFactory.getLogger(ContactController::class.java)

    @PostMapping
    fun contactAdmin(
        @RequestBody request: ContactRequestDTO
    ): ResponseEntity<ContactResponseDTO> {
        return try {

            logger.info(
                "Contact request received from email: {}",
                request.email
            )

            val response = contactService.createContactRequest(request)

            logger.info(
                "Contact request created successfully. ID: {}",
                response.id
            )

            ResponseEntity.ok(response)

        } catch (e: Exception) {

            // IMPORTANT: Print the real error in Render logs
            logger.error(
                "Failed to send contact request email",
                e
            )

            ResponseEntity.internalServerError().build()
        }

    }
}