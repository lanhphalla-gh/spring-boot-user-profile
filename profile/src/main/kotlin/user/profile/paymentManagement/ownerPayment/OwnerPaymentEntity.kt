package user.profile.paymentManagement.ownerPayment

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
@Table(name = "owner_payments")
class OwnerPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    var id: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "room_payment_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_owner_payment_room_payment")
    )
    lateinit var roomPayment: RoomPaymentEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "paid_by_user_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_owner_payment_user")
    )
    lateinit var paidByUser: UserEntity

    @Column(nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(name = "payment_method", length = 0)
    var paymentMethod: String? = null

    @Column(name = "transaction_reference", length = 150)
    var transactionReference: String? = null

    @Column(name = "payment_proof")
    var paymentProof: String? = null

    @Column(nullable = false, length = 30)
    var status: String = "PENDING"

    @Column(name = "paid_at")
    var paidAt: LocalDateTime? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}