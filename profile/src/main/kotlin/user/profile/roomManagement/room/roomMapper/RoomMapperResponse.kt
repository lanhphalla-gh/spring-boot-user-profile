package user.profile.roomManagement.room.roomMapper

import user.profile.roomManagement.room.RoomEntity
import user.profile.roomManagement.room.roomDTO.RoomResponseDTO

fun RoomEntity.toResponse(): RoomResponseDTO {
    return RoomResponseDTO(
        id = this.id!!,
        name = this.name,
        address = this.address,
        monthlyRent = this.monthlyRent,
        ownerUserId = this.ownerUserId,
        ownerQr = this.ownerQr,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}