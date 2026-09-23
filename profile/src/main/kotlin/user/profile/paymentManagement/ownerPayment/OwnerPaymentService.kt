package user.profile.paymentManagement.ownerPayment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import user.profile.paymentManagement.ownerPayment.ownerPaymentDTO.OwnerPaymentRequestDTO
import user.profile.paymentManagement.ownerPayment.ownerPaymentDTO.OwnerPaymentResponseDTO
import user.profile.roomManagement.roomPayment.RoomPaymentRepository
import user.profile.user.UserRepository
import java.math.BigDecimal
import java.util.UUID

@Service
@Transactional
class OwnerPaymentService(
    private val ownerPaymentRepository: OwnerPaymentRepository,
    private val roomPaymentRepository: RoomPaymentRepository,
    private val userRepository: UserRepository
) {
    fun create(
        request: OwnerPaymentRequestDTO
    ): OwnerPaymentResponseDTO {

        validateAmount(request.amount)

        val roomPayment = roomPaymentRepository.findById(request.roomPaymentId)
            .orElseThrow {
                RuntimeException(
                    "Room payment not found: ${request.roomPaymentId}"
                )
            }

        val paidByUser = userRepository.findById(request.paidByUserId)
            .orElseThrow {
                RuntimeException(
                    "User not found: ${request.paidByUserId}"
                )
            }

        if (
            ownerPaymentRepository.existsByRoomPaymentIdAndPaidByUserId(
                request.roomPaymentId,
                request.paidByUserId
            )
        ) {
            throw RuntimeException(
                "Owner payment already exists for this room payment and user"
            )
        }

        val entity = OwnerPaymentEntity().apply {

            this.roomPayment = roomPayment

            this.paidByUser = paidByUser

            this.amount = request.amount

            this.paymentMethod = request.paymentMethod

            this.transactionReference =
                request.transactionReference

            this.paymentProof =
                request.paymentProof

            this.status =
                request.status ?: "PENDING"

            this.paidAt =
                request.paidAt
        }

        return toResponse(
            ownerPaymentRepository.save(entity)
        )
    }

    @Transactional(readOnly = true)
    fun findById(
        id: UUID
    ): OwnerPaymentResponseDTO {

        val entity = ownerPaymentRepository.findById(id)
            .orElseThrow {
                RuntimeException(
                    "Owner payment not found: $id"
                )
            }

        return toResponse(entity)
    }

    @Transactional(readOnly = true)
    fun findAll(
        pageable: Pageable
    ): Page<OwnerPaymentResponseDTO> {

        return ownerPaymentRepository
            .findAll(pageable)
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun findByRoomPaymentId(
        roomPaymentId: UUID,
        pageable: Pageable
    ): Page<OwnerPaymentResponseDTO> {

        return ownerPaymentRepository
            .findAllByRoomPaymentId(
                roomPaymentId,
                pageable
            )
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun findByPaidByUserId(
        paidByUserId: UUID,
        pageable: Pageable
    ): Page<OwnerPaymentResponseDTO> {

        return ownerPaymentRepository
            .findAllByPaidByUserId(
                paidByUserId,
                pageable
            )
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun findByStatus(
        status: String,
        pageable: Pageable
    ): Page<OwnerPaymentResponseDTO> {

        return ownerPaymentRepository
            .findAllByStatus(
                status,
                pageable
            )
            .map(::toResponse)
    }

    @Transactional(readOnly = true)
    fun findByRoomPaymentIdAndStatus(
        roomPaymentId: UUID,
        status: String,
        pageable: Pageable
    ): Page<OwnerPaymentResponseDTO> {

        return ownerPaymentRepository
            .findAllByRoomPaymentIdAndStatus(
                roomPaymentId,
                status,
                pageable
            )
            .map(::toResponse)
    }

    fun update(
        id: UUID,
        request: OwnerPaymentRequestDTO
    ): OwnerPaymentResponseDTO {

        validateAmount(request.amount)

        val entity = ownerPaymentRepository.findById(id)
            .orElseThrow {
                RuntimeException(
                    "Owner payment not found: $id"
                )
            }

        val roomPayment = roomPaymentRepository.findById(
            request.roomPaymentId
        ).orElseThrow {
            RuntimeException(
                "Room payment not found: ${request.roomPaymentId}"
            )
        }

        val paidByUser = userRepository.findById(
            request.paidByUserId
        ).orElseThrow {
            RuntimeException(
                "User not found: ${request.paidByUserId}"
            )
        }

        entity.roomPayment = roomPayment

        entity.paidByUser = paidByUser

        entity.amount = request.amount

        entity.paymentMethod =
            request.paymentMethod

        entity.transactionReference =
            request.transactionReference

        entity.paymentProof =
            request.paymentProof

        entity.status =
            request.status ?: entity.status

        entity.paidAt =
            request.paidAt

        return toResponse(
            ownerPaymentRepository.save(entity)
        )
    }

    fun delete(
        id: UUID
    ) {

        if (!ownerPaymentRepository.existsById(id)) {
            throw RuntimeException(
                "Owner payment not found: $id"
            )
        }

        ownerPaymentRepository.deleteById(id)
    }

    private fun validateAmount(
        amount: BigDecimal
    ) {

        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException(
                "Amount must be greater than 0"
            )
        }
    }

    private fun toResponse(
        entity: OwnerPaymentEntity
    ): OwnerPaymentResponseDTO {

        return OwnerPaymentResponseDTO(

            id = entity.id!!,

            roomPaymentId =
                entity.roomPayment.id!!,

            paidByUserId =
                entity.paidByUser.id!!,

            amount =
                entity.amount,

            paymentMethod =
                entity.paymentMethod,

            transactionReference =
                entity.transactionReference,

            paymentProof =
                entity.paymentProof,

            status =
                entity.status,

            paidAt =
                entity.paidAt,

            createdAt =
                entity.createdAt,

            updatedAt =
                entity.updatedAt
        )
    }
}