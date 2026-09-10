package user.profile.contact.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import user.profile.contact.ContactRepository
import user.profile.contact.ContactRequestEntity
import user.profile.contact.contactDTO.ApproveContactRequestDTO
import user.profile.contact.contactDTO.ContactRequestDTO
import user.profile.contact.contactDTO.ContactResponseDTO
import user.profile.role.RoleRepository
import user.profile.user.UserRepository
import user.profile.user.dto.CreateUserRequestContactDTO
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
        contactRequest.status = "PENDING"

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

    fun getContactRequestList(): List<ContactResponseDTO> {

        return contactRepository.findAll()
            .map { contactRequest ->
                toResponse(contactRequest)
            }
    }


    // ========================================
    // Get Pending Request Count
    // ========================================

    fun getPendingCount(): Long {

        return contactRepository.countByStatus("PENDING")
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
    // Approve Contact Request
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
        if (contactRequest.status == "APPROVED") {
            throw RuntimeException("Contact request already approved")
        }

        // 3. Find selected role
        val role = roleRepository.findById(request.roleId)
            .orElseThrow {
                RuntimeException("Role not found ${request.roleId}")
            }


        // 4. Create User from contact Request
        val user = CreateUserRequestContactDTO(
            username = contactRequest.username,
            email = contactRequest.email,
            password = passwordEncoder.encode(request.password),
            roleId = role.id
        )

        // Save User
        userRepository.save(user)

        // 6. Update Contact Request
        contactRequest.status = "APPROVED"

        val updatedRequest =
            contactRepository.save(contactRequest)

        // 7. Send email
        contactEmailService.sendApprovedEmail(
            email = contactRequest.email!!,
            username = contactRequest.username!!,
            password = request.password
        )

        // 8. Return response
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

        contactRequest.status = "REJECTED"

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
            status = contactRequest.status!!,
            createdAt = contactRequest.createdAt,
            updatedAt = contactRequest.updatedAt
        )
    }
}