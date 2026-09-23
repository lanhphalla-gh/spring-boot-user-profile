package user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RoomLeaderAssignmentResponseDTO(
    val id: UUID,

    val roomId: UUID,

    val userId: UUID,

    val startDate: LocalDate,

    val endDate: LocalDate?,

    val status: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
