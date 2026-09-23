package user.profile.roomManagement.room

import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.roomManagement.room.roomDTO.RoomRequestDTO
import user.profile.roomManagement.room.roomDTO.RoomResponseDTO
import user.profile.roomManagement.room.roomMapper.toResponse
import java.util.UUID

@Service
class RoomService (
    private val roomRepository: RoomRepository
) {
    // =========================================
    // CREATE ROOM
    // =========================================

    @Transactional
    fun createRoom(
        request: RoomRequestDTO
    ): ResponseMessageDTO {

        // Check duplicate room name
        if (roomRepository.existsByName(request.name)) {
            throw RuntimeException(
                "Room already exists: ${request.name}"
            )
        }

        val room = RoomEntity(
            name = request.name,
            address = request.address,
            monthlyRent = request.monthlyRent,
            ownerUserId = request.ownerUserId,
            ownerQr = request.ownerQr,
            status = request.status
        )

        val savedRoom = roomRepository.save(room)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room created successfully",
            data = savedRoom
        )
    }


    // =========================================
    // GET ALL ROOMS - PAGINATION
    // =========================================

    @Transactional
    fun getListRooms(
        pageable: Pageable
    ): ResponseMessageDTO {
        val rooms = roomRepository.findAll(pageable)
            .map { room ->
                {
                    RoomResponseDTO(
                        id = room.id!!,
                        name = room.name,
                        address = room.address,
                        monthlyRent = room.monthlyRent,
                        ownerUserId = room.ownerUserId,
                        ownerQr = room.ownerQr,
                        status = room.status,
                        createdAt = room.createdAt,
                        updatedAt = room.updatedAt
                    )
                }

            }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room get successfully",
            data = rooms
        )
    }


    // =========================================
    // GET ROOM BY ID
    // =========================================

    @Transactional
    fun getRoomById(
        id: UUID
    ): ResponseMessageDTO {

        val room = roomRepository.findById(id)
            .orElse(null)
        if (room == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room not found",
            )
        }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User get successfully",
            data = room.toResponse()
        )
    }


    // =========================================
    // UPDATE ROOM
    // =========================================

    @Transactional
    fun updateRoom(
        id: UUID,
        request: RoomRequestDTO
    ): ResponseMessageDTO {

        val room = roomRepository.findById(id)
            .orElse(null)

        if (room == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room does not exists"
            )
        }

        // Check duplicate name
        val roomDuplicate = roomRepository.existsByNameAndIdNot(
            request.name,
            id
        )
        if (roomDuplicate) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room already exists: ${request.name}"
            )
        }

        // Update fields
        room.name = request.name
        room.address = request.address
        room.monthlyRent = request.monthlyRent
        room.ownerUserId = request.ownerUserId
        room.ownerQr = request.ownerQr
        room.status = request.status
        room.updatedAt = java.time.LocalDateTime.now()

        val updatedRoom = roomRepository.save(room)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room updated successfully",
            data = updatedRoom
        )
    }


    // =========================================
    // DELETE ROOM
    // =========================================

    @Transactional
    fun deleteRoom(
        id: UUID
    ) {

        val room = roomRepository.findById(id)
            .orElseThrow {
                RuntimeException(
                    "Room not found: $id"
                )
            }

        roomRepository.delete(room)
    }

    fun getRoomCount(): Long {
        return roomRepository.count()
    }
}