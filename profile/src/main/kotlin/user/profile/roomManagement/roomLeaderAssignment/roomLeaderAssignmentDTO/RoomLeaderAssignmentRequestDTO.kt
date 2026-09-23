package user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO

import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class RoomLeaderAssignmentRequestDTO(
    val id: UUID,

    @field:NotNull(message = "Room ID is required")
    var roomId: UUID,

    @field:NotNull(message = "User ID is required")
    var userId: UUID,

    @field:NotNull(message = "Start date is required")
    var startDate: LocalDate,

    val endDate: LocalDate? = null,

    val status: String = "ACTIVE"

)