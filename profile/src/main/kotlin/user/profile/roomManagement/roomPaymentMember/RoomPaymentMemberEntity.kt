package user.profile.roomManagement.roomPaymentMember

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
import user.profile.roomManagement.roomPayment.RoomPaymentEntity
import user.profile.user.UserEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "room_payment_members")
class RoomPaymentMemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "room_payment_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_room_payment_members_payment"
        )
    )
    var roomPayment: RoomPaymentEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_room_payment_members_user"
        )
    )
    var user: UserEntity,

    @Column(
        name = "expected_amount",
        nullable = false,
        precision = 10,
        scale = 2
    )
    var expectedAmount: BigDecimal = BigDecimal.ZERO,

    @Column(
        name = "paid_amount",
        nullable = false,
        precision = 10,
        scale = 2
    )
    var paidAmount: BigDecimal = BigDecimal.ZERO,

    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    var status: String = "PENDING",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)