package user.profile.paymentManagement.memberPayment.memberPaymentDTO

import java.math.BigDecimal
import java.util.UUID

data class MemberPaymentRequestDTO(
    val roomPaymentId: UUID,

    val payerUserId: UUID,

    val receiverUserId: UUID,

    val amount: BigDecimal,

    val paymentMethod: String? = null,

    val transactionReference: String? = null,

    val paymentProof: String? = null
)
