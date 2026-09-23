package user.profile.roomManagement.roomLeaderAssignment

import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.roomManagement.room.RoomRepository
import user.profile.roomManagement.roomLeaderAssignment.mapper.toResponse
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.RoomLeaderAssignmentRequestDTO
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.RoomLeaderAssignmentResponseDTO
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.UpdateRoomLeaderAssignmentRequestDTO
import user.profile.user.UserRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class RoomLeaderAssignmentService (
    private val roomLeaderAssignmentRepository:
    RoomLeaderAssignmentRepository,

    private val roomRepository:
    RoomRepository,

    private val userRepository:
    UserRepository

) {
// =========================================================
    // CREATE
    // =========================================================

    fun create(
        request: RoomLeaderAssignmentRequestDTO
    ): ResponseMessageDTO {

        // 1. Validate dates
        validateDates(
            request.startDate,
            request.endDate
        )

        // 2. Find room
        val room = roomRepository.findById(request.roomId)
            .orElse(null)
            if (room == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "Room not found: ${request.roomId}"
                )
            }

        // 3. Find user
        val user = userRepository.findById(request.userId)
            .orElse(null)
            if (user == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "User not found: ${request.userId}"
                )
            }

        // 4. Check duplicate assignment
        if (
            roomLeaderAssignmentRepository
                .existsByRoomIdAndUserIdAndStartDate(
                    request.roomId,
                    request.userId,
                    request.startDate
                )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "This user already has a leader assignment starting on ${request.startDate}"
            )
        }

        // 5. Create entity
        val roomLeader = RoomLeaderAssignmentEntity()
        roomLeader.room = room
        roomLeader.user = user
        roomLeader.startDate = request.startDate
        roomLeader.endDate = request.endDate
        roomLeader.status = request.status

        // 6. Save
        val savedRoomLeader = roomLeaderAssignmentRepository.save(roomLeader)
        val data = savedRoomLeader.toResponse()

            // 7. Response
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader created successfully",
            data = data
        )
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional
    fun getById(
        id: UUID
    ): ResponseMessageDTO {

        val roomLeaderFindById = roomLeaderAssignmentRepository.findById(id)
            .orElse(null)
        if (roomLeaderFindById == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room leader assignment not found: $id",
            )
        }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader get successfully",
            data = roomLeaderFindById.toResponse()
        )
    }

    // =========================================================
    // GET List
    // =========================================================

    @Transactional
    fun getList(pageable: Pageable): ResponseMessageDTO {

       val roomLeaders = roomLeaderAssignmentRepository.findAll(pageable)
           .map { roomLeaderAssignment ->
               RoomLeaderAssignmentResponseDTO(
                   id = roomLeaderAssignment.id!!,
                   roomId = roomLeaderAssignment.room.id!!,
                   userId = roomLeaderAssignment.user.id!!,
                   startDate = roomLeaderAssignment.startDate,
                   endDate = roomLeaderAssignment.endDate,
                   status = roomLeaderAssignment.status,
                   createdAt = roomLeaderAssignment.createdAt,
                   updatedAt = roomLeaderAssignment.updatedAt,
               )
           }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader list get successfully",
            data = roomLeaders
        )
    }


    // =========================================================
    // GET BY ROOM
    // =========================================================

    @Transactional
    fun getByRoom(
        roomId: UUID
    ): ResponseMessageDTO {

        val roomLeaders = roomRepository.existsById(roomId)

        if (!roomLeaders) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room not found: $roomId"
            )
        }

        val getRoomLeaderByUser = roomLeaderAssignmentRepository.findByRoomIdOrderByStartDateAsc(roomId)
            .map { it.toResponse() }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader get by user id get successfully",
            data = getRoomLeaderByUser
        )
    }


    // =========================================================
    // GET ACTIVE LEADER
    // =========================================================

    @Transactional
    fun getActiveLeader(
        roomId: UUID
    ): ResponseMessageDTO {
        val activeRoomLeader = roomRepository.existsById(roomId)
        if (!activeRoomLeader) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room not found: $roomId"
            )
        }

        val roomLeaderActive = roomLeaderAssignmentRepository
                .findFirstByRoomIdAndStatusOrderByStartDateDesc(
                    roomId,
                    "ACTIVE"
                )

        if (roomLeaderActive == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 404,
                message = "No active leader found for room: $roomId"
            )
        }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader active get successfully",
            data = roomLeaderActive
        )
    }

    // =========================================================
    // GET BY STATUS
    // =========================================================

    @Transactional
    fun getByStatus(
        roomId: UUID,
        status: String
    ): ResponseMessageDTO {
     val roomLeaderGetByRoom = roomRepository.existsById(roomId)
        if (!roomLeaderGetByRoom) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room not found: $roomId"
            )
        }

        val getRoomLeaderByStatus = roomLeaderAssignmentRepository
            .findByRoomIdAndStatusOrderByStartDateAsc(
                roomId,
                status
            )
            .map { it.toResponse() }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader get by status get successfully",
            data = getRoomLeaderByStatus
        )
    }


    // =========================================================
    // UPDATE
    // =========================================================

    fun update(
        id: UUID,
        request: UpdateRoomLeaderAssignmentRequestDTO
    ): ResponseMessageDTO {

        // 1. Validate dates
        validateDates(
            request.startDate,
            request.endDate
        )

        // 2. Find assignment
        val roomLeaderAssignment = roomLeaderAssignmentRepository.findById(id)
            .orElse(null)
                if (roomLeaderAssignment == null) {
                    return ResponseMessageDTO(
                        status = "Error",
                        code = 400,
                        message = "Room leader assignment not found: $id"
                    )
                }

        // 3. Find room
        val roomById = roomRepository.findById(request.roomId)
            .orElse(null)
            if (roomById == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "Room not found: ${request.roomId}"
                )
            }

        // 4. Find user
        val user = userRepository.findById(request.userId)
            .orElse(null)
            if (user == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "User not found: ${request.userId}"
                )
            }

        // 5. Update
        roomLeaderAssignment.room = roomById

        roomLeaderAssignment.user = user

        roomLeaderAssignment.startDate = request.startDate

        roomLeaderAssignment.endDate = request.endDate

        roomLeaderAssignment.status = request.status

        roomLeaderAssignment.updatedAt = LocalDateTime.now()

        // 6. Save
        val updated = roomLeaderAssignmentRepository.save(roomLeaderAssignment)
        val data = updated.toResponse()

        // 7. Response
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader updated successfully",
            data = data
        )
    }


    // =========================================================
    // DELETE
    // =========================================================

    fun delete(
        id: UUID
    ): ResponseMessageDTO {
        val roomLeaderAssignment = roomLeaderAssignmentRepository.findById(id)
            .orElse(null)

        if (roomLeaderAssignment == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room leader assignment not found: $id"
            )
        }

        roomLeaderAssignmentRepository.deleteById(id)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room leader deleted successfully"
        )
    }


    // =========================================================
    // VALIDATE DATES
    // =========================================================

    private fun validateDates(
        startDate: java.time.LocalDate,
        endDate: java.time.LocalDate?
    ) {

        if (endDate != null && endDate.isBefore(startDate)) {
            throw RuntimeException(
                "End date cannot be before start date"
            )
        }
    }

    // =========================================
    // COUNT
    // =========================================

    fun getRoomCount(): Long {
        return roomRepository.count()
    }
}