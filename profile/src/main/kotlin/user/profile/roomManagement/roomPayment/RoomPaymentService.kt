package user.profile.roomManagement.roomPayment

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import user.profile.roomManagement.room.RoomRepository
import user.profile.roomManagement.roomPayment.roomPaymentDTO.RoomPaymentRequestDTO
import user.profile.roomManagement.roomPayment.roomPaymentDTO.RoomPaymentResponseDTO
import user.profile.user.UserRepository
import java.util.UUID

@Service
class RoomPaymentService(
    private val roomPaymentRepository: RoomPaymentRepository,
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(request: RoomPaymentRequestDTO): RoomPaymentResponseDTO {

        val roomId = request.roomId
            ?: throw IllegalArgumentException("Room ID is required")

        val paymentType = request.paymentType
            ?: throw IllegalArgumentException("Payment type is required")

        val paymentMonth = request.paymentMonth
            ?: throw IllegalArgumentException("Payment month is required")

        val totalAmount = request.totalAmount
            ?: throw IllegalArgumentException("Total amount is required")

        val room = roomRepository.findById(roomId)
            .orElseThrow {
                IllegalArgumentException("Room not found: $roomId")
            }

        val duplicate =
            roomPaymentRepository.existsByRoom_IdAndPaymentTypeAndPaymentMonth(
                roomId = roomId,
                paymentType = paymentType,
                paymentMonth = paymentMonth
            )

        if (duplicate) {
            throw IllegalArgumentException(
                "Payment already exists for this room, payment type and month"
            )
        }

        val responsibleUser = request.responsibleUserId?.let { userId ->
            userRepository.findById(userId)
                .orElseThrow {
                    IllegalArgumentException("Responsible user not found: $userId")
                }
        }

        val entity = RoomPaymentEntity(
            room = room,
            paymentType = paymentType.uppercase(),
            paymentMonth = paymentMonth,
            totalAmount = totalAmount,
            responsibleUser = responsibleUser,
            description = request.description,
            dueDate = request.dueDate,
            status = request.status?.uppercase() ?: "PENDING"
        )

        val saved = roomPaymentRepository.save(entity)

        return toResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getById(id: UUID): RoomPaymentResponseDTO {

        val payment = roomPaymentRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException("Room payment not found: $id")
            }

        return toResponse(payment)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<RoomPaymentResponseDTO> {

        return roomPaymentRepository
            .findAllByOrderByPaymentMonthDescCreatedAtDesc()
            .map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getList(
        page: Int,
        size: Int
    ): Page<RoomPaymentResponseDTO> {

        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                Sort.Order.desc("paymentMonth"),
                Sort.Order.desc("createdAt")
            )
        )

        return roomPaymentRepository
            .findAll(pageable)
            .map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getByRoom(
        roomId: UUID
    ): List<RoomPaymentResponseDTO> {

        roomRepository.findById(roomId)
            .orElseThrow {
                IllegalArgumentException("Room not found: $roomId")
            }

        return roomPaymentRepository
            .findAllByRoom_IdOrderByPaymentMonthDescCreatedAtDesc(roomId)
            .map { toResponse(it) }
    }

    @Transactional
    fun update(
        id: UUID,
        request: RoomPaymentRequestDTO
    ): RoomPaymentResponseDTO {

        val payment = roomPaymentRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException("Room payment not found: $id")
            }

        val roomId = request.roomId
            ?: throw IllegalArgumentException("Room ID is required")

        val paymentType = request.paymentType
            ?: throw IllegalArgumentException("Payment type is required")

        val paymentMonth = request.paymentMonth
            ?: throw IllegalArgumentException("Payment month is required")

        val totalAmount = request.totalAmount
            ?: throw IllegalArgumentException("Total amount is required")

        val room = roomRepository.findById(roomId)
            .orElseThrow {
                IllegalArgumentException("Room not found: $roomId")
            }

        val duplicate =
            roomPaymentRepository
                .existsByRoom_IdAndPaymentTypeAndPaymentMonthAndIdNot(
                    roomId = roomId,
                    paymentType = paymentType,
                    paymentMonth = paymentMonth,
                    id = id
                )

        if (duplicate) {
            throw IllegalArgumentException(
                "Payment already exists for this room, payment type and month"
            )
        }

        val responsibleUser = request.responsibleUserId?.let { userId ->
            userRepository.findById(userId)
                .orElseThrow {
                    IllegalArgumentException("Responsible user not found: $userId")
                }
        }

        payment.room = room
        payment.paymentType = paymentType.uppercase()
        payment.paymentMonth = paymentMonth
        payment.totalAmount = totalAmount
        payment.responsibleUser = responsibleUser
        payment.description = request.description
        payment.dueDate = request.dueDate
        payment.status = request.status?.uppercase() ?: "PENDING"

        val saved = roomPaymentRepository.save(payment)

        return toResponse(saved)
    }

    @Transactional
    fun delete(id: UUID) {

        val payment = roomPaymentRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException("Room payment not found: $id")
            }

        roomPaymentRepository.delete(payment)
    }

    private fun toResponse(
        entity: RoomPaymentEntity
    ): RoomPaymentResponseDTO {

        return RoomPaymentResponseDTO(
            id = entity.id!!,
            roomId = entity.room.id!!,
            roomName = entity.room.name,
            paymentType = entity.paymentType,
            paymentMonth = entity.paymentMonth,
            totalAmount = entity.totalAmount,
            responsibleUserId = entity.responsibleUser?.id,
            responsibleUserName = entity.responsibleUser?.username,
            description = entity.description,
            dueDate = entity.dueDate,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}