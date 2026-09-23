package user.profile.paymentManagement.paymentRotation.paymentRotationDTO

import java.time.LocalDateTime
import java.util.UUID

data class PaymentRotationResponseDTO(
    val id: UUID,

    val roomId: UUID,

    val userId: UUID,

    val rotationOrder: Int,

    val status: Boolean,

    val paymentRotationStatus: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
