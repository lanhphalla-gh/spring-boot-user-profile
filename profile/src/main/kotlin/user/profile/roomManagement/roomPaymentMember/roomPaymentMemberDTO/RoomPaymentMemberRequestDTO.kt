package user.profile.roomManagement.roomPaymentMember.roomPaymentMemberDTO

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID

data class RoomPaymentMemberRequestDTO(
    @field:NotNull(message = "Room payment ID is required")
    val roomPaymentId: UUID?,

    @field:NotNull(message = "User ID is required")
    val userId: UUID?,

    @field:NotNull(message = "Expected amount is required")
    @field:DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Expected amount must be greater than or equal to 0"
    )
    val expectedAmount: BigDecimal?,

    @field:NotNull(message = "Paid amount is required")
    @field:DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Paid amount must be greater than or equal to 0"
    )
    val paidAmount: BigDecimal?,

    @field:Size(
        max = 30,
        message = "Status must not exceed 30 characters"
    )
    val status: String? = "PENDING"
)
