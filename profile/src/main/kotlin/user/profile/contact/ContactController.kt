package user.profile.contact

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/contact-admin")
class ContactController(
    private val contactEmailService: ContactEmailService
) {
    private val logger =
        LoggerFactory.getLogger(ContactController::class.java)

    @PostMapping
    fun contactAdmin(
        @RequestBody request: ContactRequest
    ): ResponseEntity<Map<String, String>> {
        return try {
            logger.info(
                "Contact request received from email: {}",
                request.email
            )

            contactEmailService.sendContactRequest(request)

            logger.info(
                "Contact request email sent successfully"
            )

            ResponseEntity.ok(
                mapOf(
                    "Message" to "Your request has been sent successfully!"
                )
            )
        } catch (e: Exception) {

            // IMPORTANT: Print the real error in Render logs
            logger.error(
                "Failed to send contact request email",
                e
            )
            ResponseEntity.internalServerError().body(
                mapOf(
                    "message" to "Failed to send your request. Please try again later."
                )
            )
        }

    }
}