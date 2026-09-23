package user.profile.roomManagement.room

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "rooms")
class RoomEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var address: String? = null,

    @Column(name = "monthly_rent", nullable = false)
    var monthlyRent: Double = 0.0,

    @Column(name = "owner_user_id", nullable = false)
    var ownerUserId: UUID,

    @Column(name = "owner_qr", columnDefinition = "TEXT")
    var ownerQr: String? = null,

    @Column(nullable = false, length = 20)
    var status: Boolean = true,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)