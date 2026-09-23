package user.profile.roomManagement.roomLeaderAssignment.mapper

import user.profile.roomManagement.roomLeaderAssignment.RoomLeaderAssignmentEntity
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.RoomLeaderAssignmentResponseDTO

fun RoomLeaderAssignmentEntity.toResponse(): RoomLeaderAssignmentResponseDTO {
    return RoomLeaderAssignmentResponseDTO(
        id = this.id!!,
        roomId = this.room.id!!,
        userId = this.user.id!!,
        startDate = startDate,
        endDate = this.endDate,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt

    )
}