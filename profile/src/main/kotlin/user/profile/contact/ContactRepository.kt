package user.profile.contact

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import user.profile.contact.contactEnum.ContactRequestStatus
import java.util.UUID

@Repository
interface ContactRepository: JpaRepository<ContactRequestEntity, UUID> {
    fun countByStatus(status: ContactRequestStatus): Long
}