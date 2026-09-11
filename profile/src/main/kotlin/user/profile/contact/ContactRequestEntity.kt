package user.profile.contact

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import user.profile.contact.contactEnum.ContactRequestStatus
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "contact_requests")
class ContactRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @Column(name = "full_name", nullable = false)
    var fullName: String? = null

    @Column(nullable = false)
    var email: String? = null

    @Column(nullable = false)
    var username: String? = null

    @Column(columnDefinition = "TEXT")
    var message: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ContactRequestStatus = ContactRequestStatus.PENDING

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
}