package user.profile.roomManagement.roomPayment.roomPaymentDTO

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RoomPaymentResponseDTO(
    val id: UUID,

    val roomId: UUID,

    val roomName: String,

    val paymentType: String,

    val paymentMonth: LocalDate,

    val totalAmount: BigDecimal,

    val responsibleUserId: UUID?,

    val responsibleUserName: String?,

    val description: String?,

    val dueDate: LocalDate?,

    val status: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
