package user.profile.roomManagement.room.roomDTO

import java.util.UUID

data class RoomRequestDTO(
    val name: String,

    val address: String? = null,

    val monthlyRent: Double = 0.0,

    val ownerUserId: UUID,

    val ownerQr: String? = null,

    val status: Boolean = true
)
