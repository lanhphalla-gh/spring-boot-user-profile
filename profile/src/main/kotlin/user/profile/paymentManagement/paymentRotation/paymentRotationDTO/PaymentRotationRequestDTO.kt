package user.profile.paymentManagement.paymentRotation.paymentRotationDTO

import java.util.UUID

data class PaymentRotationRequestDTO(
    val roomId: UUID,

    val userId: UUID,

    val rotationOrder: Int,

    val paymentRotationStatus: String = "ACTIVE"
)
