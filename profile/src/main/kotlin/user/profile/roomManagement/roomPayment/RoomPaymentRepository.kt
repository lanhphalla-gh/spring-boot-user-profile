package user.profile.roomManagement.roomPayment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface RoomPaymentRepository : JpaRepository<RoomPaymentEntity, UUID> {
    fun findAllByOrderByPaymentMonthDescCreatedAtDesc(
        pageable: Pageable
    ): Page<RoomPaymentEntity>

    fun findAllByOrderByPaymentMonthDescCreatedAtDesc(): List<RoomPaymentEntity>

    fun findAllByRoom_IdOrderByPaymentMonthDescCreatedAtDesc(
        roomId: UUID,
        pageable: Pageable
    ): Page<RoomPaymentEntity>

    fun findAllByRoom_IdOrderByPaymentMonthDescCreatedAtDesc(
        roomId: UUID
    ): List<RoomPaymentEntity>

    fun existsByRoom_IdAndPaymentTypeAndPaymentMonth(
        roomId: UUID,
        paymentType: String,
        paymentMonth: LocalDate
    ): Boolean

    fun existsByRoom_IdAndPaymentTypeAndPaymentMonthAndIdNot(
        roomId: UUID,
        paymentType: String,
        paymentMonth: LocalDate,
        id: UUID
    ): Boolean

    fun countByStatus(status: String): Long
}