package user.profile.paymentManagement.paymentRotation

import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import user.profile.messageDTO.ResponseMessageDTO
import user.profile.paymentManagement.paymentRotation.paymentRotationDTO.PaymentRotationRequestDTO
import user.profile.paymentManagement.paymentRotation.paymentRotationDTO.PaymentRotationResponseDTO
import java.time.LocalDateTime
import java.util.UUID
import user.profile.paymentManagement.paymentRotation.paymentRotationMapper.toResponse

@Service
class PaymentRotationService(
    private val paymentRotationRepository: PaymentRotationRepository
) {
    // =========================================
    // CREATE
    // =========================================

    @Transactional
    fun createPaymentRotation(
        request: PaymentRotationRequestDTO
    ): ResponseMessageDTO {

        // rotation order must be greater than 0
        if (request.rotationOrder <= 0) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Rotation order must be greater than 0"
            )
        }

        // Check same user already exists in this room
        if (
            paymentRotationRepository.existsByRoomIdAndUserId(
                roomId = request.roomId,
                userId = request.userId
            )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User already exists in payment rotation"
            )
        }

        // Check same rotation order
        if (
            paymentRotationRepository
                .existsByRoomIdAndRotationOrder(
                    roomId = request.roomId,
                    rotationOrder = request.rotationOrder
                )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Rotation order ${request.rotationOrder} already exists"
            )
        }

        val rotation = PaymentRotationEntity()
        rotation.roomId = request.roomId
        rotation.userId = request.userId
        rotation.rotationOrder = request.rotationOrder
        rotation.paymentRotationStatus = request.paymentRotationStatus


        val savedRotation = paymentRotationRepository.save(rotation)
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Payment rotation created successfully",
            data = savedRotation
        )
    }

    // =========================================
    // GET ALL
    // PAGINATION
    // =========================================

    @Transactional
    fun getAllPaymentRotations(
        pageable: Pageable)
    : ResponseMessageDTO {
    val paymentRotation = paymentRotationRepository.findAll(pageable)
        .map { getPaymentRotation ->
            PaymentRotationResponseDTO(
                id = getPaymentRotation.id!!,
                roomId = getPaymentRotation.roomId!!,
                userId = getPaymentRotation.userId!!,
                rotationOrder = getPaymentRotation.rotationOrder!!,
                status = getPaymentRotation.status!!,
                paymentRotationStatus = getPaymentRotation.paymentRotationStatus,
                createdAt = LocalDateTime.now(),
                updatedAt = getPaymentRotation.updatedAt
        ) }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Payment rotation get successfully",
            data = paymentRotation
        )
    }

    // =========================================
    // GET BY ROOM
    // PAGINATION
    // =========================================

    @Transactional
    fun getByRoom(
        roomId: UUID,
        pageable: Pageable
    ): ResponseMessageDTO {

        val paymentRotation = paymentRotationRepository
            .findByRoomId(
                roomId = roomId,
                pageable = pageable
            )
            .map { paymentRotationGetByRoom ->
                PaymentRotationResponseDTO(
                    id = paymentRotationGetByRoom.id!!,
                    roomId = paymentRotationGetByRoom.roomId!!,
                    userId = paymentRotationGetByRoom.userId!!,
                    rotationOrder = paymentRotationGetByRoom.rotationOrder!!,
                    status = paymentRotationGetByRoom.status!!,
                    paymentRotationStatus = paymentRotationGetByRoom.paymentRotationStatus,
                    createdAt = LocalDateTime.now(),
                    updatedAt = paymentRotationGetByRoom.updatedAt
                )
            }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Payment rotation get by room get successfully",
            data = paymentRotation
        )
    }


    // =========================================
    // GET ACTIVE BY ROOM
    // PAGINATION
    // =========================================

    @Transactional
    fun getActiveByRoom(
        roomId: UUID,
        pageable: Pageable
    ): ResponseMessageDTO {

        val paymentRotationGetStatusByRooms = paymentRotationRepository
            .findByRoomIdAndStatus(
                roomId = roomId,
                status = true
            )
            .map { paymentRotationGetStatusByRoom ->
                PaymentRotationResponseDTO(
                    id = paymentRotationGetStatusByRoom.id!!,
                    roomId = paymentRotationGetStatusByRoom.roomId!!,
                    userId = paymentRotationGetStatusByRoom.userId!!,
                    rotationOrder = paymentRotationGetStatusByRoom.rotationOrder!!,
                    status = paymentRotationGetStatusByRoom.status!!,
                    paymentRotationStatus = paymentRotationGetStatusByRoom.paymentRotationStatus,
                    createdAt = LocalDateTime.now(),
                    updatedAt = paymentRotationGetStatusByRoom.updatedAt
                )
            }
        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Payment rotation get active by room get successfully",
            data = paymentRotationGetStatusByRooms
        )
    }


    // =========================================
    // GET BY ID
    // =========================================

    @Transactional
    fun getById(id: UUID): ResponseMessageDTO {

        val rotationGetById = paymentRotationRepository.findById(id)
            .orElse(null)

        if (rotationGetById == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Payment rotation not found: $id",
            )
        }

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "Payment rotation get successfully",
            data = rotationGetById.toResponse()
        )
    }


    // =========================================
    // UPDATE
    // =========================================

    @Transactional
    fun updatePaymentRotation(
        id: UUID,
        request: PaymentRotationRequestDTO
    ): ResponseMessageDTO {

        if (request.rotationOrder <= 0) {
            throw RuntimeException(
                "Rotation order must be greater than 0"
            )
        }

        val rotation = paymentRotationRepository.findById(id)
                .orElse(null)

        if (rotation == null) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Payment rotation not found: $id"
            )
        }

        // Check duplicate user
        if (
            paymentRotationRepository
                .existsByRoomIdAndUserIdAndIdNot(
                    roomId = request.roomId,
                    userId = request.userId,
                    id = id
                )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "User already exists in payment rotation"
            )
        }

        // Check duplicate order
        if (
            paymentRotationRepository
                .existsByRoomIdAndRotationOrderAndIdNot(
                    roomId = request.roomId,
                    rotationOrder = request.rotationOrder,
                    id = id
                )
        ) {
            return ResponseMessageDTO(
                status = "Error",
                code = 400,
                message = "Rotation order ${request.rotationOrder} already exists"
            )
        }

        rotation.roomId = request.roomId
        rotation.userId = request.userId
        rotation.rotationOrder = request.rotationOrder
        rotation.paymentRotationStatus = request.paymentRotationStatus
        rotation.updatedAt = LocalDateTime.now()

        val updatedRotation = paymentRotationRepository.save(rotation)

        return ResponseMessageDTO(
            status = "Success",
            code = 200,
            message = "User updated successfully",
            data = updatedRotation
        )
    }


    // =========================================
    // DELETE
    // =========================================

    @Transactional
    fun deletePaymentRotation(
        id: UUID
    ) {

        val rotation =
            paymentRotationRepository.findById(id)
                .orElseThrow {
                    RuntimeException(
                        "Payment rotation not found: $id"
                    )
                }

        paymentRotationRepository.delete(rotation)
    }
}