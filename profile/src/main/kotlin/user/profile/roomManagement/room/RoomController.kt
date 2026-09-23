package user.profile.roomManagement.room

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.roomManagement.room.roomDTO.RoomRequestDTO

import java.util.UUID

@RestController
@RequestMapping("/api/rooms")
class RoomController (
    private val roomService: RoomService
) {
    // =========================================
    // CREATE
    // =========================================

    @PostMapping
    fun createRoom(
        @RequestBody request: RoomRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {

        val room = roomService.createRoom(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ResponseMessageDTO(
                    status = "Success",
                    code = 201,
                    message = "Room created successfully",
                    data = room
                )
            )
    }


    // =========================================
    // GET ALL - PAGINATION
    // =========================================

    @GetMapping
    fun getAllRooms(
        @PageableDefault(
            page = 0,
            size = 10
        )
        pageable: Pageable
    ): ResponseEntity<ResponseMessageDTO> {

        val rooms = roomService.getListRooms(pageable)

        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Rooms retrieved successfully",
                data = rooms
            )
        )
    }


    // =========================================
    // GET BY ID
    // =========================================

    @GetMapping("/{id}")
    fun getRoomById(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {

        val room = roomService.getRoomById(id)

        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room retrieved successfully",
                data = room
            )
        )
    }


    // =========================================
    // UPDATE
    // =========================================

    @PutMapping("/{id}")
    fun updateRoom(
        @PathVariable id: UUID,
        @RequestBody request: RoomRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {

        val room = roomService.updateRoom(
            id = id,
            request = request
        )

        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room updated successfully",
                data = room
            )
        )
    }


    // =========================================
    // DELETE
    // =========================================

    @DeleteMapping("/{id}")
    fun deleteRoom(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {

        roomService.deleteRoom(id)

        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room deleted successfully",
                data = null
            )
        )
    }

    // =========================================
    // COUNT
    // =========================================
    @GetMapping("/count")
    fun getRoomCount(): ResponseEntity<Long> {

        val count = roomService.getRoomCount()
        return ResponseEntity.ok(count)

    }
}