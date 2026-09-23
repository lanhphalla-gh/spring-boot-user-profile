package user.profile.roomManagement.roomPaymentMember.roomPaymentMemberDTO

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class RoomPaymentMemberResponseDTO(
    val id: UUID,

    val roomPaymentId: UUID,

    val userId: UUID,

    val username: String?,

    val expectedAmount: BigDecimal,

    val paidAmount: BigDecimal,

    val remainingAmount: BigDecimal,

    val status: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
