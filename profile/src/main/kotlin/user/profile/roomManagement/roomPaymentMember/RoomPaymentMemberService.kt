package user.profile.roomManagement.roomPaymentMember

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import user.profile.roomManagement.roomPayment.RoomPaymentRepository
import user.profile.roomManagement.roomPaymentMember.roomPaymentMemberDTO.RoomPaymentMemberRequestDTO
import user.profile.roomManagement.roomPaymentMember.roomPaymentMemberDTO.RoomPaymentMemberResponseDTO
import user.profile.user.UserRepository
import java.math.BigDecimal
import java.util.UUID

@Service
class RoomPaymentMemberService(
    private val roomPaymentMemberRepository: RoomPaymentMemberRepository,
    private val roomPaymentRepository: RoomPaymentRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(
        request: RoomPaymentMemberRequestDTO
    ): RoomPaymentMemberResponseDTO {

        val roomPaymentId = request.roomPaymentId
            ?: throw IllegalArgumentException(
                "Room payment ID is required"
            )

        val userId = request.userId
            ?: throw IllegalArgumentException(
                "User ID is required"
            )

        val expectedAmount = request.expectedAmount
            ?: throw IllegalArgumentException(
                "Expected amount is required"
            )

        val paidAmount = request.paidAmount
            ?: throw IllegalArgumentException(
                "Paid amount is required"
            )

        // Validate payment
        val roomPayment = roomPaymentRepository.findById(roomPaymentId)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment not found: $roomPaymentId"
                )
            }

        // Validate user
        val user = userRepository.findById(userId)
            .orElseThrow {
                IllegalArgumentException(
                    "User not found: $userId"
                )
            }

        // Validate paid amount
        validatePaidAmount(
            expectedAmount,
            paidAmount
        )

        // Check duplicate
        val duplicate =
            roomPaymentMemberRepository
                .existsByRoomPayment_IdAndUser_Id(
                    roomPaymentId,
                    userId
                )

        if (duplicate) {
            throw IllegalArgumentException(
                "This user is already assigned to this room payment"
            )
        }

        val entity = RoomPaymentMemberEntity(
            roomPayment = roomPayment,
            user = user,
            expectedAmount = expectedAmount,
            paidAmount = paidAmount,
            status = calculateStatus(
                expectedAmount,
                paidAmount
            )
        )

        val saved = roomPaymentMemberRepository.save(entity)

        return toResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getById(
        id: UUID
    ): RoomPaymentMemberResponseDTO {

        val entity = roomPaymentMemberRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment member not found: $id"
                )
            }

        return toResponse(entity)
    }

    @Transactional(readOnly = true)
    fun getAll(): List<RoomPaymentMemberResponseDTO> {

        return roomPaymentMemberRepository
            .findAllByOrderByCreatedAtDesc()
            .map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getList(
        page: Int,
        size: Int
    ): Page<RoomPaymentMemberResponseDTO> {

        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                Sort.Order.desc("createdAt")
            )
        )

        return roomPaymentMemberRepository
            .findAllByOrderByCreatedAtDesc(pageable)
            .map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getByRoomPayment(
        roomPaymentId: UUID
    ): List<RoomPaymentMemberResponseDTO> {

        roomPaymentRepository.findById(roomPaymentId)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment not found: $roomPaymentId"
                )
            }

        return roomPaymentMemberRepository
            .findAllByRoomPayment_IdOrderByCreatedAtDesc(
                roomPaymentId
            )
            .map { toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getByUser(
        userId: UUID
    ): List<RoomPaymentMemberResponseDTO> {

        userRepository.findById(userId)
            .orElseThrow {
                IllegalArgumentException(
                    "User not found: $userId"
                )
            }

        return roomPaymentMemberRepository
            .findAllByUser_IdOrderByCreatedAtDesc(userId)
            .map { toResponse(it) }
    }

    @Transactional
    fun update(
        id: UUID,
        request: RoomPaymentMemberRequestDTO
    ): RoomPaymentMemberResponseDTO {

        val entity = roomPaymentMemberRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment member not found: $id"
                )
            }

        val roomPaymentId = request.roomPaymentId
            ?: throw IllegalArgumentException(
                "Room payment ID is required"
            )

        val userId = request.userId
            ?: throw IllegalArgumentException(
                "User ID is required"
            )

        val expectedAmount = request.expectedAmount
            ?: throw IllegalArgumentException(
                "Expected amount is required"
            )

        val paidAmount = request.paidAmount
            ?: throw IllegalArgumentException(
                "Paid amount is required"
            )

        val roomPayment = roomPaymentRepository.findById(roomPaymentId)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment not found: $roomPaymentId"
                )
            }

        val user = userRepository.findById(userId)
            .orElseThrow {
                IllegalArgumentException(
                    "User not found: $userId"
                )
            }

        validatePaidAmount(
            expectedAmount,
            paidAmount
        )

        val duplicate =
            roomPaymentMemberRepository
                .existsByRoomPayment_IdAndUser_IdAndIdNot(
                    roomPaymentId,
                    userId,
                    id
                )

        if (duplicate) {
            throw IllegalArgumentException(
                "This user is already assigned to this room payment"
            )
        }

        entity.roomPayment = roomPayment
        entity.user = user
        entity.expectedAmount = expectedAmount
        entity.paidAmount = paidAmount

        // Calculate automatically
        entity.status = calculateStatus(
            expectedAmount,
            paidAmount
        )

        entity.updatedAt = java.time.LocalDateTime.now()

        val saved = roomPaymentMemberRepository.save(entity)

        return toResponse(saved)
    }

    @Transactional
    fun delete(
        id: UUID
    ) {

        val entity = roomPaymentMemberRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "Room payment member not found: $id"
                )
            }

        roomPaymentMemberRepository.delete(entity)
    }

    private fun validatePaidAmount(
        expectedAmount: BigDecimal,
        paidAmount: BigDecimal
    ) {

        if (paidAmount > expectedAmount) {
            throw IllegalArgumentException(
                "Paid amount cannot be greater than expected amount"
            )
        }
    }

    private fun calculateStatus(
        expectedAmount: BigDecimal,
        paidAmount: BigDecimal
    ): String {

        return when {
            paidAmount.compareTo(BigDecimal.ZERO) == 0 ->
                "PENDING"

            paidAmount < expectedAmount ->
                "PARTIAL"

            paidAmount.compareTo(expectedAmount) == 0 ->
                "PAID"

            else ->
                "PENDING"
        }
    }

    private fun toResponse(
        entity: RoomPaymentMemberEntity
    ): RoomPaymentMemberResponseDTO {

        val remainingAmount =
            entity.expectedAmount - entity.paidAmount

        return RoomPaymentMemberResponseDTO(
            id = entity.id!!,
            roomPaymentId = entity.roomPayment.id!!,
            userId = entity.user.id!!,
            username = entity.user.username,
            expectedAmount = entity.expectedAmount,
            paidAmount = entity.paidAmount,
            remainingAmount = remainingAmount,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}