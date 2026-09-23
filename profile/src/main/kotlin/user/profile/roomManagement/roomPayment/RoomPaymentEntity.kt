package user.profile.roomManagement.roomPayment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import user.profile.roomManagement.room.RoomEntity
import user.profile.user.UserEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "room_payments")
class RoomPaymentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "room_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_room_payments_room")
    )
    var room: RoomEntity,

    @Column(name = "payment_type", nullable = false, length = 30)
    var paymentType: String,

    @Column(name = "payment_month", nullable = false)
    var paymentMonth: LocalDate,

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "responsible_user_id",
        foreignKey = ForeignKey(name = "fk_room_payments_responsible_user")
    )
    var responsibleUser: UserEntity? = null,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "due_date")
    var dueDate: LocalDate? = null,

    @Column(name = "status", nullable = false, length = 30)
    var status: String = "PENDING",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)