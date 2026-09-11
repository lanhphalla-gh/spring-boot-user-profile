package user.profile.contact.service

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.contact.ContactRepository
import user.profile.contact.ContactRequestEntity
import user.profile.contact.contactEnum.ContactRequestStatus
import user.profile.contact.contactDTO.ApproveContactRequestDTO
import user.profile.contact.contactDTO.ContactRequestCountResponseDTO
import user.profile.contact.contactDTO.ContactRequestDTO
import user.profile.contact.contactDTO.ContactResponseDTO
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.role.RoleRepository
import user.profile.user.UserRepository
import user.profile.user.UserEntity
import java.util.UUID

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val contactEmailService: ContactEmailService,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) {

    // ========================================
    // Create Contact Request
    // ========================================

    fun createContactRequest(
        request: ContactRequestDTO
    ): ContactResponseDTO {

        // Create entity from request
        val contactRequest = ContactRequestEntity()

        contactRequest.fullName = request.fullName
        contactRequest.email = request.email
        contactRequest.username = request.username
        contactRequest.message = request.message
        contactRequest.status = ContactRequestStatus.PENDING

        // Save request to database
        val savedRequest =
            contactRepository.save(contactRequest)

        // Send email to admin
        contactEmailService.sendContactRequestEmail(request)

        // Convert entity to response DTO
        return toResponse(savedRequest)
    }


    // ========================================
    // Get Contact Request List
    // ========================================

    fun getContactRequestList(pageable: Pageable): ResponseMessageDTO{
        val contactRequestList = contactRepository.findAll(pageable)
            .map { contactRequest ->
                ContactResponseDTO(
                    id = contactRequest.id!!,
                    fullName = contactRequest.fullName!!,
                    email = contactRequest.email!!,
                    username = contactRequest.username!!,
                    message = contactRequest.message,
                    status = contactRequest.status.name,
                    createdAt = contactRequest.createdAt,
                    updatedAt = contactRequest.updatedAt,
                )
            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Contact request list get successfully",
            data = contactRequestList
        )
    }


    // ========================================
    // Get Pending Request Count
    // ========================================

    fun getContactRequestCount(): ContactRequestCountResponseDTO {

        val approved =
            contactRepository.countByStatus(ContactRequestStatus.APPROVED)

        val pending =
            contactRepository.countByStatus(ContactRequestStatus.PENDING)

        val rejected =
            contactRepository.countByStatus(ContactRequestStatus.REJECTED)

        return ContactRequestCountResponseDTO(
            approved = approved,
            pending = pending,
            rejected = rejected
        )
    }


    // ========================================
    // Get Contact Request By ID
    // ========================================

    fun getContactRequestById(
        id: UUID
    ): ContactResponseDTO {

        val contactRequest =
            contactRepository.findById(id)
                .orElseThrow {
                    RuntimeException(
                        "Contact request not found: $id"
                    )
                }

        return toResponse(contactRequest)
    }


    // ========================================
    // Approved Contact Request
    // ========================================

    fun approveContactRequest(
        id: UUID,
        request: ApproveContactRequestDTO
    ): ContactResponseDTO {

        // 1. Find contact request
        val contactRequest =
            contactRepository.findById(id)
                .orElseThrow {
                    RuntimeException(
                        "Contact request not found: $id"
                    )
                }

        // 2. Check already approved
        if (contactRequest.status == ContactRequestStatus.APPROVED) {
            throw RuntimeException("Contact request already approved")
        }

        // 3. Check username already exists
        if (userRepository.existsByUsername(contactRequest.username!!)) {
            throw RuntimeException(
                "Username already exists: ${contactRequest.username}"
            )
        }

        // 4. Check email already exists
        if (userRepository.existsByEmail(contactRequest.email!!)) {
            throw RuntimeException(
                "Email already exists: ${contactRequest.email}"
            )
        }

        // 5. Find selected role
        val role = roleRepository.findById(request.roleId)
            .orElseThrow {
                RuntimeException(
                    "Role not found: ${request.roleId}"
                )
            }

        // 6. Create UserEntity
        val user = UserEntity()

        user.username = contactRequest.username
        user.email = contactRequest.email
        user.password = passwordEncoder.encode(request.password)
        user.role = role

        // 7. Save user
        userRepository.save(user)

        // 8. Update Contact Request
        contactRequest.status = ContactRequestStatus.APPROVED

        val updatedRequest =
            contactRepository.save(contactRequest)

        // 9. Send email
        contactEmailService.sendApprovedEmail(
            email = contactRequest.email!!,
            username = contactRequest.username!!,
            password = request.password
        )

        // 10. Return response
        return toResponse(updatedRequest)
    }


    // ========================================
    // Reject Contact Request
    // ========================================

    fun rejectContactRequest(
        id: UUID
    ): ContactResponseDTO {

        val contactRequest =
            contactRepository.findById(id)
                .orElseThrow {
                    RuntimeException(
                        "Contact request not found: $id"
                    )
                }

        contactRequest.status = ContactRequestStatus.REJECTED

        val updatedRequest =
            contactRepository.save(contactRequest)

        return toResponse(updatedRequest)
    }


    // ========================================
    // Entity → Response DTO
    // ========================================

    private fun toResponse(
        contactRequest: ContactRequestEntity
    ): ContactResponseDTO {

        return ContactResponseDTO(
            id = contactRequest.id!!,
            fullName = contactRequest.fullName!!,
            email = contactRequest.email!!,
            username = contactRequest.username!!,
            message = contactRequest.message,
            status = contactRequest.status.name,
            createdAt = contactRequest.createdAt,
            updatedAt = contactRequest.updatedAt
        )
    }
}