package user.profile.roomManagement.room.roomDTO

import java.time.LocalDateTime
import java.util.UUID

data class RoomResponseDTO(
    val id: UUID,

    val name: String,

    val address: String?,

    val monthlyRent: Double,

    val ownerUserId: UUID,

    val ownerQr: String?,

    val status: Boolean,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)
