package user.profile.paymentManagement.paymentRotation.paymentRotationMapper

import user.profile.paymentManagement.paymentRotation.PaymentRotationEntity
import user.profile.paymentManagement.paymentRotation.paymentRotationDTO.PaymentRotationResponseDTO

fun PaymentRotationEntity.toResponse(): PaymentRotationResponseDTO {
    return PaymentRotationResponseDTO(
        id = this.id!!,
        roomId = this.roomId!!,
        userId = this.userId!!,
        rotationOrder = this.rotationOrder!!,
        status = this.status!!,
        paymentRotationStatus = this.paymentRotationStatus,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}