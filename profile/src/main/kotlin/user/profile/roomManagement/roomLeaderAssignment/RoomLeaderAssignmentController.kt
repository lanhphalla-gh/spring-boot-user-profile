package user.profile.roomManagement.roomLeaderAssignment

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
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.RoomLeaderAssignmentRequestDTO
import user.profile.roomManagement.roomLeaderAssignment.roomLeaderAssignmentDTO.UpdateRoomLeaderAssignmentRequestDTO
import java.util.UUID

@RestController
@RequestMapping("/api/room")
class RoomLeaderAssignmentController(
    private val roomLeaderAssignmentService: RoomLeaderAssignmentService
) {
    // =========================================
    // CREATE
    // =========================================
    @PostMapping("/create")
    fun createRoomLeaderAssignment(
        @RequestBody request: RoomLeaderAssignmentRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {
        val roomLeaderAssign = roomLeaderAssignmentService.create(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ResponseMessageDTO(
                    status = "Success",
                    code = 201,
                    message = "Room leader assign created successfully",
                    data = roomLeaderAssign
                )
            )
    }

    // =========================================
    // GET List - PAGINATION
    // =========================================

    @GetMapping("/list")
    fun getRoomLeaderAssignments(
        @PageableDefault(
            page = 0,
            size = 10)
        pageable: Pageable
    ): ResponseEntity<ResponseMessageDTO> {
        val roomLeaderAssign = roomLeaderAssignmentService.getList(pageable)
        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room leader assigns retrieved successfully",
                data = roomLeaderAssign
            )
        )
    }

    // =========================================
    // GET BY ID
    // =========================================

    @GetMapping("/{id}")
    fun getRoomLeaderAssignmentByID(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {
        val roomLeaderAssign = roomLeaderAssignmentService.getById(id)
        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room leader assigns retrieved successfully",
                data = roomLeaderAssign
            )
        )
    }

    // =========================================
    // UPDATE
    // =========================================

    @PutMapping("/{id}")
    fun updateRoomLeaderAssignment(
        @PathVariable id: UUID,
        @RequestBody request: UpdateRoomLeaderAssignmentRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {
        val roomLeaderAssign = roomLeaderAssignmentService.update(id, request)

        return ResponseEntity.ok(
            ResponseMessageDTO(
                status = "Success",
                code = 200,
                message = "Room leader assigns updated successfully",
                data = roomLeaderAssign
            )
        )
    }

    // =========================================
    // DELETE
    // =========================================

    @DeleteMapping("/{id}")
    fun deleteRoomLeaderAssignment(
        @PathVariable id: UUID
    ): ResponseEntity<ResponseMessageDTO> {
        roomLeaderAssignmentService.delete(id)
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
    fun getRoomLeaderAssignCount(): ResponseEntity<Long> {
        val count = roomLeaderAssignmentService.getRoomCount()
        return ResponseEntity.ok(count)
    }


}