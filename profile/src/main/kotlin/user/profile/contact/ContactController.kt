package user.profile.contact

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = arrayOf("/api/contact-admin"))
class ContactController(
    private val contactEmailService: ContactEmailService
) {
    @PostMapping
    fun contactAdmin(
        @RequestBody request: ContactRequest
    ): ResponseEntity<Map<String, String>> {
        return try {
            contactEmailService.sendContactRequest(request)

            ResponseEntity.ok(
                mapOf(
                    "Message" to "Your request has been sent successfully!"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                mapOf(
                    "message" to "Failed to send your request. Please try again later."
                )
            )
        }
    }
}