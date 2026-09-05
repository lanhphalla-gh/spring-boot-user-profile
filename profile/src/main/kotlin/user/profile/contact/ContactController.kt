package user.profile.contact

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.contact.contactDTO.ContactRequestDTO
import user.profile.contact.contactDTO.ContactResponseDTO
import user.profile.contact.service.ContactService
import java.util.UUID

@RestController
@RequestMapping("/api")
class ContactController(
    private val contactService: ContactService
) {
    private val logger =
        LoggerFactory.getLogger(ContactController::class.java)

    @PostMapping("/contact-request")
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

    @GetMapping("/contact-request/list")
    fun getContactRequestList(): ResponseEntity<List<ContactResponseDTO>> {

        return ResponseEntity.ok(
            contactService.getContactRequestList()
        )
    }

    // ========================================
    // Get Pending Request Count
    // ========================================

    @GetMapping("/contact-request/pending/count")
    fun getPendingCount():
            ResponseEntity<Map<String, Long>> {

        val count =
            contactService.getPendingCount()

        return ResponseEntity.ok(
            mapOf(
                "count" to count
            )
        )
    }


    // ========================================
    // Get Contact Request By ID
    // ========================================

    @GetMapping("/contact-request/{id}")
    fun getContactRequestById(
        @PathVariable id: UUID
    ): ResponseEntity<ContactResponseDTO> {

        val response =
            contactService.getContactRequestById(id)

        return ResponseEntity.ok(response)
    }


    // ========================================
    // Approve Contact Request
    // ========================================

    @PutMapping("/contact-request/{id}/approve")
    fun approveContactRequest(
        @PathVariable id: UUID
    ): ResponseEntity<ContactResponseDTO> {

        val response =
            contactService.approveContactRequest(id)

        return ResponseEntity.ok(response)
    }


    // ========================================
    // Reject Contact Request
    // ========================================

    @PutMapping("/contact-request/{id}/reject")
    fun rejectContactRequest(
        @PathVariable id: UUID
    ): ResponseEntity<ContactResponseDTO> {

        val response =
            contactService.rejectContactRequest(id)

        return ResponseEntity.ok(response)
    }
}