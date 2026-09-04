package user.profile.contact.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import user.profile.contact.contactDTO.ContactRequestDTO

@Service
class ContactEmailService(
    @Value("\${resend.api-key}")
    private val resendApiKey: String,

    @Value("\${resend.from-email}")
    private val fromEmail: String,

    @Value("\${resend.admin-email}")
    private val adminEmail: String
) {
    private val restClient = RestClient.builder()
        .baseUrl("https://api.resend.com")
        .build()

    fun sendContactRequestEmail(request: ContactRequestDTO) {

        val subject =
            "User Account Request - ${request.username}"

        val htmlBody = """
            <h2>New User Account Request</h2>

            <p>A new user has requested an account.</p>

            <hr>

            <p>
                <strong>Full Name:</strong>
                ${request.fullName}
            </p>

            <p>
                <strong>Email:</strong>
                ${request.email}
            </p>

            <p>
                <strong>Requested Username:</strong>
                ${request.username}
            </p>

            <p>
                <strong>Message:</strong>
            </p>

            <p>
                ${request.message}
            </p>

            <hr>

            <p>
                Please review this request and create the account
                if the user is approved.
            </p>
        """.trimIndent()


        restClient.post()
            .uri("/emails")
            .header(
                "Authorization",
                "Bearer $resendApiKey"
            )
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                mapOf(
                    "from" to fromEmail,
                    "to" to listOf(adminEmail),
                    "subject" to subject,
                    "html" to htmlBody
                )
            )
            .retrieve()
            .toBodilessEntity()
    }
}