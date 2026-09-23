package user.profile.roomManagement.roomLeaderAssignment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface RoomLeaderAssignmentRepository: JpaRepository<RoomLeaderAssignmentEntity, UUID> {
    // Get all assignments of a room
    fun findByRoomIdOrderByStartDateAsc(
        roomId: UUID
    ): List<RoomLeaderAssignmentEntity>


    // Get assignments by status
    fun findByRoomIdAndStatusOrderByStartDateAsc(
        roomId: UUID,
        status: String
    ): List<RoomLeaderAssignmentEntity>


    // Get active leader
    fun findFirstByRoomIdAndStatusOrderByStartDateDesc(
        roomId: UUID,
        status: String
    ): RoomLeaderAssignmentEntity?


    // Check whether user already has an assignment
    fun existsByRoomIdAndUserIdAndStartDate(
        roomId: UUID,
        userId: UUID,
        startDate: LocalDate
    ): Boolean
}