package user.profile.roomManagement.roomMember

import org.springframework.data.domain.Pageable
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
import user.profile.roomManagement.roomMember.roomMemberDTO.RoomMemberRequestDTO
import java.util.UUID

@RestController
@RequestMapping("/api/room-members")
class RoomMemberController(
    private val roomMemberService: RoomMemberService
) {
    // =========================================
    // CREATE
    // =========================================

    @PostMapping("/create")
    fun createRoomMember(
        @RequestBody request: RoomMemberRequestDTO
    ): ResponseEntity<ResponseMessageDTO> {

        val roomMember = roomMemberService.createRoomMember(request)

        return ResponseEntity
            .status(roomMember.code)
            .body(roomMember)
    }

    // =========================================
    // GET ALL
    // PAGINATION
    // =========================================

    @GetMapping("/list")
    fun getAllRoomMembers(pageable: Pageable): ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.getAllRoomMembers(pageable)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // GET BY ROOM
    // =========================================

    @GetMapping("/room/{roomId}")
    fun getMembersByRoom(
        @PathVariable roomId: UUID,
        pageable: Pageable)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.getMembersByRoom(roomId, pageable)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // GET ACTIVE MEMBERS BY ROOM
    // =========================================

    @GetMapping("/room/{roomId}/active")
    fun getActiveMembersByRoom(
        @PathVariable roomId: UUID,
        pageable: Pageable)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.getActiveMembersByRoom(roomId, pageable)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }


    // =========================================
    // GET BY USER
    // =========================================

    @GetMapping("/user/{userId}")
    fun getMembersByUser(
        @PathVariable userId: UUID,
        pageable: Pageable)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.getMembersByUser(userId, pageable)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // GET BY ID
    // =========================================

    @GetMapping("/{id}")
    fun getRoomMemberById(
        @PathVariable id: UUID)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.getRoomMemberById(id)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // UPDATE
    // =========================================

    @PutMapping("/{id}")
    fun updateRoomMember(
        @PathVariable id: UUID,
        @RequestBody request: RoomMemberRequestDTO)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.updateRoomMember(id, request)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // LEAVE ROOM
    // =========================================

    @PutMapping("/{id}/leave")
    fun leaveRoom(
        @PathVariable id: UUID)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.leaveRoom(id)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

    // =========================================
    // DELETE
    // =========================================

    @DeleteMapping("/{id}")
    fun deleteRoomMember(
        @PathVariable id: UUID)
    : ResponseEntity<ResponseMessageDTO> {

        val roomMembers = roomMemberService.deleteRoomMember(id)

        return ResponseEntity
            .status(roomMembers.code)
            .body(roomMembers)
    }

}