package user.profile.paymentManagement.ownerPayment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OwnerPaymentRepository : JpaRepository<OwnerPaymentEntity, UUID> {
    fun findAllByRoomPaymentId(
        roomPaymentId: UUID,
        pageable: Pageable
    ): Page<OwnerPaymentEntity>

    fun findAllByPaidByUserId(
        paidByUserId: UUID,
        pageable: Pageable
    ): Page<OwnerPaymentEntity>

    fun findAllByStatus(
        status: String,
        pageable: Pageable
    ): Page<OwnerPaymentEntity>

    fun findAllByRoomPaymentIdAndStatus(
        roomPaymentId: UUID,
        status: String,
        pageable: Pageable
    ): Page<OwnerPaymentEntity>

    fun existsByRoomPaymentIdAndPaidByUserId(
        roomPaymentId: UUID,
        paidByUserId: UUID
    ): Boolean
}