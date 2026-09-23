package user.profile.paymentManagement.ownerPayment.ownerPaymentDTO

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class OwnerPaymentRequestDTO(

    val roomPaymentId: UUID,

    val paidByUserId: UUID,

    val amount: BigDecimal,

    val paymentMethod: String? = null,

    val transactionReference: String? = null,

    val paymentProof: String? = null,

    val status: String? = "PENDING",

    val paidAt: LocalDateTime? = null
)
