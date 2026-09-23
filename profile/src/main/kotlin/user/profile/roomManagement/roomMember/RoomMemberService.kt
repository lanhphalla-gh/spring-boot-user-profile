package user.profile.roomManagement.roomMember

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.roomManagement.roomMember.roomMemberDTO.RoomMemberRequestDTO
import user.profile.roomManagement.roomMember.roomMemberDTO.RoomMemberResponseDTO
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class RoomMemberService (
    private val roomMemberRepository: RoomMemberRepository
) {

    // =========================================
    // CREATE ROOM MEMBER
    // =========================================

    @Transactional
    fun createRoomMember(
        request: RoomMemberRequestDTO
    ): ResponseMessageDTO {

        // Check if user is already an ACTIVE member
        if (
            roomMemberRepository.existsByRoomIdAndUserIdAndMemberStatus(
                roomId = request.roomId,
                userId = request.userId,
                memberStatus = "ACTIVE"
            )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User is already an active member of this room"
            )
        }

        val roomMember = RoomMemberEntity()
        roomMember.roomId = request.roomId
        roomMember.userId = request.userId
        roomMember.joinedDate = request.joinedDate ?: LocalDate.now()
        roomMember.leftDate = null
        roomMember.status = true
        roomMember.memberStatus = request.memberStatus


        val savedMember = roomMemberRepository.save(roomMember)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member created successfully",
            data = savedMember
        )
    }


    // =========================================
    // GET ALL ROOM MEMBERS
    // PAGINATION
    // =========================================

    @Transactional
    fun getAllRoomMembers(
        pageable: Pageable
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findAll(pageable)
            .map { roomMember ->
                RoomMemberResponseDTO(
                    id = roomMember.id!!,
                    roomId = roomMember.roomId!!,
                    userId = roomMember.userId!!,
                    joinedDate = roomMember.joinedDate,
                    leftDate = roomMember.leftDate,
                    status = roomMember.status,
                    memberStatus = roomMember.memberStatus!!,
                    createdAt = roomMember.createdAt,
                    updatedAt = roomMember.updatedAt,

                )

            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member get successfully",
            data = roomMember
        )
    }


    // =========================================
    // GET ROOM MEMBERS BY ROOM
    // PAGINATION
    // =========================================

    @Transactional
    fun getMembersByRoom(
        roomId: UUID,
        pageable: Pageable
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findByRoomId(roomId, pageable)
            .map { roomMember ->
                RoomMemberResponseDTO(
                    id = roomMember.id!!,
                    roomId = roomMember.roomId!!,
                    userId = roomMember.userId!!,
                    joinedDate = roomMember.joinedDate,
                    leftDate = roomMember.leftDate,
                    status = roomMember.status,
                    memberStatus = roomMember.memberStatus!!,
                    createdAt = roomMember.createdAt,
                    updatedAt = roomMember.updatedAt,
                )
            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member get by room successfully",
            data = roomMember
        )
    }


    // =========================================
    // GET ACTIVE MEMBERS BY ROOM
    // PAGINATION
    // =========================================

    @Transactional
    fun getActiveMembersByRoom(
        roomId: UUID,
        pageable: Pageable
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findByRoomIdAndMemberStatus(roomId, "ACTIVE", pageable)
            .map { roomMember ->
                RoomMemberResponseDTO(
                    id = roomMember.id!!,
                    roomId = roomMember.roomId!!,
                    userId = roomMember.userId!!,
                    joinedDate = roomMember.joinedDate,
                    leftDate = roomMember.leftDate,
                    status = roomMember.status,
                    memberStatus = roomMember.memberStatus!!,
                    createdAt = roomMember.createdAt,
                    updatedAt = roomMember.updatedAt,
                )
            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member get by status successfully",
            data = roomMember
        )
    }


    // =========================================
    // GET MEMBERS BY USER
    // PAGINATION
    // =========================================

    @Transactional
    fun getMembersByUser(
        userId: UUID,
        pageable: Pageable
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findByUserId(userId, pageable)
            .map { roomMember ->
                RoomMemberResponseDTO(
                    id = roomMember.id!!,
                    roomId = roomMember.roomId!!,
                    userId = roomMember.userId!!,
                    joinedDate = roomMember.joinedDate,
                    leftDate = roomMember.leftDate,
                    status = roomMember.status,
                    memberStatus = roomMember.memberStatus!!,
                    createdAt = roomMember.createdAt,
                    updatedAt = roomMember.updatedAt,
                )
            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member get by user successfully",
            data = roomMember
        )
    }


    // =========================================
    // GET BY ID
    // =========================================

    @Transactional
    fun getRoomMemberById(
        id: UUID
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findById(id)
            .map { roomMember ->
            RoomMemberResponseDTO(
                id = roomMember.id!!,
                roomId = roomMember.roomId!!,
                userId = roomMember.userId!!,
                joinedDate = roomMember.joinedDate,
                leftDate = roomMember.leftDate,
                status = roomMember.status,
                memberStatus = roomMember.memberStatus!!,
                createdAt = roomMember.createdAt,
                updatedAt = roomMember.updatedAt,
            )
        }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member get by id successfully",
            data = roomMember
        )
    }


    // =========================================
    // UPDATE ROOM MEMBER
    // =========================================

    @Transactional
    fun updateRoomMember(
        id: UUID,
        request: RoomMemberRequestDTO
    ): ResponseMessageDTO {

        val roomMember = roomMemberRepository.findById(id)
            .orElse(null)
            if (roomMember == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "Room member does not exists"
                )
            }

        // Do not allow another ACTIVE record

        val existsByRoomIdAndUserIdAndStatus = roomMemberRepository
            .existsByRoomIdAndUserIdAndMemberStatus(
                roomId = request.roomId,
                userId = request.userId,
                memberStatus = "ACTIVE"
            )

        if ( request.status && existsByRoomIdAndUserIdAndStatus ) {
            // Allow the current record to update itself
            if (
                roomMember.roomId != request.roomId ||
                roomMember.userId != request.userId || !roomMember.status
            ) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "User is already an active member of this room"
                )
            }
        }

        roomMember.roomId = request.roomId
        roomMember.userId = request.userId
        roomMember.joinedDate = request.joinedDate ?: roomMember.joinedDate
        roomMember.leftDate = request.leftDate
        roomMember.status = request.status
        roomMember.memberStatus = request.memberStatus
        roomMember.updatedAt = LocalDateTime.now()

        val updatedMember = roomMemberRepository.save(roomMember)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Room member updated successfully",
            data = updatedMember
        )
    }

    // =========================================
    // REMOVE / LEAVE ROOM
    // =========================================

    @Transactional
    fun leaveRoom(
        id: UUID
    ): ResponseMessageDTO {

        val member = roomMemberRepository.findById(id)
            .orElse(null)

        if (member == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Room member not found: $id"
            )
        }

        member.memberStatus = "LEFT"
        member.leftDate = LocalDate.now()
        member.updatedAt = LocalDateTime.now()

        val updatedMember =
            roomMemberRepository.save(member)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User remove/leaf room successfully",
            data = updatedMember
        )
    }

    // =========================================
    // DELETE
    // =========================================

    @Transactional
    fun deleteRoomMember(
        id: UUID
    ): ResponseMessageDTO {

        val member = roomMemberRepository.findById(id)
            .orElse(null)

            if (member == null) {
                return ResponseMessageDTO(
                    status = "Error",
                    code = 400,
                    message = "Room member not found: $id"
                )
            }

        val deleteRoomMember = roomMemberRepository.delete(member)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Deleted room member successfully",
            data = deleteRoomMember
        )
    }
}