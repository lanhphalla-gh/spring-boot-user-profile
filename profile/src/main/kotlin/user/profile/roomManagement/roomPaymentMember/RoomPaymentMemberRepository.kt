package user.profile.roomManagement.roomPaymentMember

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoomPaymentMemberRepository: JpaRepository<RoomPaymentMemberEntity, UUID> {
    fun findAllByOrderByCreatedAtDesc(
        pageable: Pageable
    ): Page<RoomPaymentMemberEntity>

    fun findAllByOrderByCreatedAtDesc():
            List<RoomPaymentMemberEntity>

    fun findAllByRoomPayment_IdOrderByCreatedAtDesc(
        roomPaymentId: UUID
    ): List<RoomPaymentMemberEntity>

    fun findAllByUser_IdOrderByCreatedAtDesc(
        userId: UUID
    ): List<RoomPaymentMemberEntity>

    fun existsByRoomPayment_IdAndUser_Id(
        roomPaymentId: UUID,
        userId: UUID
    ): Boolean

    fun existsByRoomPayment_IdAndUser_IdAndIdNot(
        roomPaymentId: UUID,
        userId: UUID,
        id: UUID
    ): Boolean
}