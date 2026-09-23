package user.profile.paymentManagement.paymentRotation

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentRotationRepository: JpaRepository<PaymentRotationEntity, UUID> {
    fun existsByRoomIdAndUserId(
        roomId: UUID,
        userId: UUID
    ): Boolean

    fun existsByRoomIdAndRotationOrder(
        roomId: UUID,
        rotationOrder: Int
    ): Boolean

    fun existsByRoomIdAndUserIdAndIdNot(
        roomId: UUID,
        userId: UUID,
        id: UUID
    ): Boolean

    fun existsByRoomIdAndRotationOrderAndIdNot(
        roomId: UUID,
        rotationOrder: Int,
        id: UUID
    ): Boolean

    fun findByRoomId(
        roomId: UUID,
        pageable: Pageable
    ): List<PaymentRotationEntity>

    fun findByRoomIdAndStatus(
        roomId: UUID, status: Boolean
    ): List<PaymentRotationEntity>
}