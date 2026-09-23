package user.profile.roomManagement.roomMember

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface RoomMemberRepository: JpaRepository<RoomMemberEntity, UUID> {
    fun existsByRoomIdAndUserIdAndMemberStatus(
        roomId: UUID, userId: UUID, memberStatus: String
    ): Boolean

    fun findByRoomId(
        roomId: UUID,
        pageable: Pageable
    ): Page<RoomMemberEntity>

    fun findByUserId(
        userId: UUID,
        pageable: Pageable
    ): Page<RoomMemberEntity>

    fun findByRoomIdAndMemberStatus(
        roomId: UUID, memberStatus: String, pageable: Pageable
    ): Page<RoomMemberEntity>
}