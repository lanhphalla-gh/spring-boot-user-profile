package user.profile.contact

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
class ContactEmailService(
    private val mailSender: MailSender
) {
    private val adminEmail = "lanh1.phalla24@gmail.com"

    fun sendContactRequest(request: ContactRequest) {
        val mail = SimpleMailMessage()

        // Email recipient
        mail.setTo(adminEmail)

        // Email subject
        mail.subject = "User Account Request - ${request.username}"

        // Email body
        mail.text = """
                        Hello Admin,

            A new user has requested an account.

            Full Name: ${'$'}{request.fullName}
            Email: ${'$'}{request.email}
            Requested Username: ${'$'}{request.username}

            Message:
            ${'$'}{request.message}

            Please review this request and create the account if approved.

            Thank you.
        """.trimIndent()

        // Send email
        mailSender.send(mail)
    }
}