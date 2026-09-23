package user.profile.roomManagement.roomMember.roomMemberDTO

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RoomMemberResponseDTO(
    val id: UUID,

    val roomId: UUID,

    val userId: UUID,

    val joinedDate: LocalDate,

    val leftDate: LocalDate?,

    val status: Boolean,

    val memberStatus: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
