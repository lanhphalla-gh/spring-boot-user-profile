package user.profile.paymentManagement.ownerPayment

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.paymentManagement.ownerPayment.ownerPaymentDTO.OwnerPaymentRequestDTO
import user.profile.paymentManagement.ownerPayment.ownerPaymentDTO.OwnerPaymentResponseDTO
import java.util.UUID

@RestController
@RequestMapping("/api/owner-payments")
class OwnerPaymentController(
    private val ownerPaymentService: OwnerPaymentService
) {
    @PostMapping
    fun create(
        @RequestBody request: OwnerPaymentRequestDTO
    ): ResponseEntity<OwnerPaymentResponseDTO> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ownerPaymentService.create(request)
            )
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID
    ): ResponseEntity<OwnerPaymentResponseDTO> {

        return ResponseEntity.ok(
            ownerPaymentService.findById(id)
        )
    }

    @GetMapping
    fun findAll(
        pageable: Pageable
    ): ResponseEntity<Page<OwnerPaymentResponseDTO>> {

        return ResponseEntity.ok(
            ownerPaymentService.findAll(pageable)
        )
    }

    @GetMapping("/room-payment/{roomPaymentId}")
    fun findByRoomPaymentId(
        @PathVariable roomPaymentId: UUID,
        pageable: Pageable
    ): ResponseEntity<Page<OwnerPaymentResponseDTO>> {

        return ResponseEntity.ok(
            ownerPaymentService.findByRoomPaymentId(
                roomPaymentId,
                pageable
            )
        )
    }

    @GetMapping("/paid-by/{userId}")
    fun findByPaidByUserId(
        @PathVariable userId: UUID,
        pageable: Pageable
    ): ResponseEntity<Page<OwnerPaymentResponseDTO>> {

        return ResponseEntity.ok(
            ownerPaymentService.findByPaidByUserId(
                userId,
                pageable
            )
        )
    }

    @GetMapping("/status/{status}")
    fun findByStatus(
        @PathVariable status: String,
        pageable: Pageable
    ): ResponseEntity<Page<OwnerPaymentResponseDTO>> {

        return ResponseEntity.ok(
            ownerPaymentService.findByStatus(
                status,
                pageable
            )
        )
    }

    @GetMapping("/room-payment/{roomPaymentId}/status/{status}")
    fun findByRoomPaymentIdAndStatus(
        @PathVariable roomPaymentId: UUID,
        @PathVariable status: String,
        pageable: Pageable
    ): ResponseEntity<Page<OwnerPaymentResponseDTO>> {

        return ResponseEntity.ok(
            ownerPaymentService.findByRoomPaymentIdAndStatus(
                roomPaymentId,
                status,
                pageable
            )
        )
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: OwnerPaymentRequestDTO
    ): ResponseEntity<OwnerPaymentResponseDTO> {

        return ResponseEntity.ok(
            ownerPaymentService.update(
                id,
                request
            )
        )
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {

        ownerPaymentService.delete(id)

        return ResponseEntity.noContent().build()
    }
}