package user.profile.paymentManagement.ownerPayment.ownerPaymentDTO

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class OwnerPaymentResponseDTO(
    val id: UUID,

    val roomPaymentId: UUID,

    val paidByUserId: UUID,

    val amount: BigDecimal,

    val paymentMethod: String?,

    val transactionReference: String?,

    val paymentProof: String?,

    val status: String,

    val paidAt: LocalDateTime?,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
