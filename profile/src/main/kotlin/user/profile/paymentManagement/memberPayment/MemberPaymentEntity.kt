package user.profile.paymentManagement.memberPayment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
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
@Table(name = "member_payments")
class MemberPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "room_payment_id",
        nullable = false,
        foreignKey = jakarta.persistence.ForeignKey(
            name = "fk_member_payment_room_payment"
        )
    )
    lateinit var roomPayment: RoomPaymentEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "payer_user_id",
        nullable = false,
        foreignKey = jakarta.persistence.ForeignKey(
            name = "fk_member_payment_payer"
        )
    )
    lateinit var payer: UserEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "receiver_user_id",
        nullable = false,
        foreignKey = jakarta.persistence.ForeignKey(
            name = "fk_member_payment_receiver"
        )
    )
    lateinit var receiver: UserEntity

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    lateinit var amount: BigDecimal

    @Column(name = "payment_method", length = 50)
    var paymentMethod: String? = null

    @Column(name = "transaction_reference", length = 150)
    var transactionReference: String? = null

    @Column(name = "payment_proof", columnDefinition = "TEXT")
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