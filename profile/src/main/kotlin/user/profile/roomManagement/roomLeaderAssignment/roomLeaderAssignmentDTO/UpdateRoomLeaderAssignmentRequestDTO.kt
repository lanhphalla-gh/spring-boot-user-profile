package user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO

import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class UpdateRoomLeaderAssignmentRequestDTO(
    @field:NotNull(message = "Room ID is required")
    var roomId: UUID,

    @field:NotNull(message = "User ID is required")
    var userId: UUID,

    @field:NotNull(message = "Start date is required")
    var startDate: LocalDate,

    var endDate: LocalDate? = null,

    var status: String
)
