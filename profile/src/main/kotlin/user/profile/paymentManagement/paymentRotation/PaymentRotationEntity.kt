package user.profile.paymentManagement.paymentRotation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "payment_rotation")
class PaymentRotationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @Column(name = "room_id", nullable = false)
    var roomId: UUID? = null

    @Column(name = "user_id", nullable = false)
    var userId: UUID? = null

    @Column(name = "rotation_order", nullable = false)
    var rotationOrder: Int? = null

    @Column(nullable = false, length = 20)
    var status: Boolean? = null

    @Column(nullable = false, length = 20)
    var paymentRotationStatus: String = "ACTIVE"// ===> Add new column

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}