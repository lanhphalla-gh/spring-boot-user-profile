package user.profile.roomManagement.roomMember.roomMemberDTO

import java.time.LocalDate
import java.util.UUID

data class RoomMemberRequestDTO(
    val roomId: UUID,

    val userId: UUID,

    val joinedDate: LocalDate? = null,

    val leftDate: LocalDate? = null,

    val status: Boolean = true,

    val memberStatus: String? = null,
)
