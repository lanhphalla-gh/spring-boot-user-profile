package user.profile.paymentManagement.memberPayment

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import user.profile.paymentManagement.memberPayment.memberPaymentDTO.MemberPaymentRequestDTO
import user.profile.paymentManagement.memberPayment.memberPaymentDTO.MemberPaymentResponseDTO
import user.profile.roomManagement.roomPayment.RoomPaymentRepository
import user.profile.user.UserRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class MemberPaymentService (
    private val memberPaymentRepository: MemberPaymentRepository,
    private val roomPaymentRepository: RoomPaymentRepository,
    private val userRepository: UserRepository
){

    fun create(
        request: MemberPaymentRequestDTO
    ): MemberPaymentResponseDTO {

        if (request.amount <= java.math.BigDecimal.ZERO) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }

        if (request.payerUserId == request.receiverUserId) {
            throw IllegalArgumentException(
                "Payer and receiver cannot be the same user"
            )
        }

        val roomPayment = roomPaymentRepository
            .findById(request.roomPaymentId)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment not found: ${request.roomPaymentId}"
                )
            }

        val payer = userRepository
            .findById(request.payerUserId)
            .orElseThrow {
                IllegalArgumentException(
                    "Payer user not found: ${request.payerUserId}"
                )
            }

        val receiver = userRepository
            .findById(request.receiverUserId)
            .orElseThrow {
                IllegalArgumentException(
                    "Receiver user not found: ${request.receiverUserId}"
                )
            }

        val memberShipPayment = MemberPaymentEntity()
        memberShipPayment.roomPayment.id = request.roomPaymentId
        memberShipPayment.payer.id = request.payerUserId
        memberShipPayment.receiver.id = request.receiverUserId
        memberShipPayment.amount = request.amount
        memberShipPayment.paymentMethod = request.paymentMethod
        memberShipPayment.paymentProof = request.paymentProof
        memberShipPayment.status = "PENDING"
        memberShipPayment.paidAt   = null
        memberShipPayment.createdAt = LocalDateTime.now()
        memberShipPayment.updatedAt = LocalDateTime.now()

        return toResponse(
            memberPaymentRepository.save(memberShipPayment)
        )
    }

    @Transactional(readOnly = true)
    fun getById(
        id: UUID
    ): MemberPaymentResponseDTO {

        val entity = memberPaymentRepository
            .findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Member payment not found: $id"
                )
            }

        return toResponse(entity)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<MemberPaymentResponseDTO> {

        return memberPaymentRepository
            .findAll()
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun getByRoomPayment(
        roomPaymentId: UUID
    ): List<MemberPaymentResponseDTO> {

        return memberPaymentRepository
            .findByRoomPaymentId(roomPaymentId)
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun getByPayer(
        payerUserId: UUID
    ): List<MemberPaymentResponseDTO> {

        return memberPaymentRepository
            .findByPayerId(payerUserId)
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun getByReceiver(
        receiverUserId: UUID
    ): List<MemberPaymentResponseDTO> {

        return memberPaymentRepository
            .findByReceiverId(receiverUserId)
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun getByStatus(
        status: String
    ): List<MemberPaymentResponseDTO> {

        return memberPaymentRepository
            .findByStatus(status)
            .map(::toResponse)
    }

    fun confirm(
        id: UUID
    ): MemberPaymentResponseDTO {

        val entity = findEntity(id)

        if (entity.status != "PENDING") {
            throw IllegalArgumentException(
                "Only PENDING payments can be confirmed"
            )
        }

        entity.status = "CONFIRMED"
        entity.paidAt = LocalDateTime.now()
        entity.updatedAt = LocalDateTime.now()

        return toResponse(
            memberPaymentRepository.save(entity)
        )
    }

    fun reject(
        id: UUID
    ): MemberPaymentResponseDTO {

        val entity = findEntity(id)

        if (entity.status != "PENDING") {
            throw IllegalArgumentException(
                "Only PENDING payments can be rejected"
            )
        }

        entity.status = "REJECTED"
        entity.updatedAt = LocalDateTime.now()

        return toResponse(
            memberPaymentRepository.save(entity)
        )
    }

    fun cancel(
        id: UUID
    ): MemberPaymentResponseDTO {

        val entity = findEntity(id)

        if (entity.status != "PENDING") {
            throw IllegalArgumentException(
                "Only PENDING payments can be cancelled"
            )
        }

        entity.status = "CANCELLED"
        entity.updatedAt = LocalDateTime.now()

        return toResponse(
            memberPaymentRepository.save(entity)
        )
    }

    private fun findEntity(
        id: UUID
    ): MemberPaymentEntity {

        return memberPaymentRepository
            .findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Member payment not found: $id"
                )
            }
    }

    private fun toResponse(
        entity: MemberPaymentEntity
    ): MemberPaymentResponseDTO {

        return MemberPaymentResponseDTO(
            id = entity.id!!,

            roomPaymentId = entity.roomPayment.id!!,

            payerUserId = entity.payer.id!!,
            payerName = entity.payer.username,

            receiverUserId = entity.receiver.id!!,
            receiverName = entity.receiver.username,

            amount = entity.amount,

            paymentMethod = entity.paymentMethod,

            transactionReference =
                entity.transactionReference,

            paymentProof =
                entity.paymentProof,

            status = entity.status,

            paidAt = entity.paidAt,

            createdAt = entity.createdAt,

            updatedAt = entity.updatedAt
        )
    }
}