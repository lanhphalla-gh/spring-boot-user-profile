package user.profile.paymentManagement.memberPayment.memberPaymentDTO

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class MemberPaymentResponseDTO(
    val id: UUID,

    val roomPaymentId: UUID,

    val payerUserId: UUID,

    val payerName: String?,

    val receiverUserId: UUID,

    val receiverName: String?,

    val amount: BigDecimal,

    val paymentMethod: String?,

    val transactionReference: String?,

    val paymentProof: String?,

    val status: String,

    val paidAt: LocalDateTime?,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
