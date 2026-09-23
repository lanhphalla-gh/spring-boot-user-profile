package user.profile.paymentManagement.memberPayment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberPaymentRepository: JpaRepository<MemberPaymentEntity, UUID> {
    fun findByRoomPaymentId(roomPaymentId: UUID): List<MemberPaymentEntity>

    fun findByPayerId(payerUserId: UUID): List<MemberPaymentEntity>

    fun findByReceiverId(receiverUserId: UUID): List<MemberPaymentEntity>

    fun findByStatus(status: String): List<MemberPaymentEntity>
}