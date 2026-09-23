package user.profile.roomManagement.roomMember

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "room_members")
class RoomMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @Column(name = "room_id", nullable = false)
    var roomId: UUID? = null

    @Column(name = "user_id", nullable = false)
    var userId: UUID? = null

    @Column(name = "joined_date", nullable = false)
    var joinedDate: LocalDate = LocalDate.now()

    @Column(name = "left_date")
    var leftDate: LocalDate? = null

    @Column(nullable = false, length = 20)
    var status: Boolean = true

    @Column(nullable = false, length = 20)
    var memberStatus: String? = "ACTIVE"

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}