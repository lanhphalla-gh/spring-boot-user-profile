package user.profile.roomManagement.roomPayment.roomPaymentDTO

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class RoomPaymentRequestDTO(
    @field:NotNull(message = "Room ID is required")
    val roomId: UUID?,

    @field:NotBlank(message = "Payment type is required")
    @field:Size(max = 30, message = "Payment type must not exceed 30 characters")
    val paymentType: String?,

    @field:NotNull(message = "Payment month is required")
    val paymentMonth: LocalDate?,

    @field:NotNull(message = "Total amount is required")
    @field:DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "Total amount must be greater than or equal to 0"
    )
    val totalAmount: BigDecimal?,

    val responsibleUserId: UUID?,

    val description: String?,

    val dueDate: LocalDate?,

    @field:Size(max = 30, message = "Status must not exceed 30 characters")
    val status: String? = "PENDING"
)
