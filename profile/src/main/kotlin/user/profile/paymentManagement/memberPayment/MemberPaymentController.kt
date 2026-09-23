package user.profile.paymentManagement.memberPayment

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import user.profile.paymentManagement.memberPayment.memberPaymentDTO.MemberPaymentRequestDTO
import user.profile.paymentManagement.memberPayment.memberPaymentDTO.MemberPaymentResponseDTO
import java.util.UUID

@RestController
@RequestMapping("/api/member-payments")
class MemberPaymentController(
    private val memberPaymentService: MemberPaymentService
) {
    @PostMapping
    fun create(
        @RequestBody request: MemberPaymentRequestDTO
    ): ResponseEntity<MemberPaymentResponseDTO> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                memberPaymentService.create(request)
            )
    }

    @GetMapping
    fun getAll():
            ResponseEntity<List<MemberPaymentResponseDTO>> {

        return ResponseEntity.ok(
            memberPaymentService.getAll()
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID
    ): ResponseEntity<MemberPaymentResponseDTO> {

        return ResponseEntity.ok(
            memberPaymentService.getById(id)
        )
    }

    @GetMapping("/room-payment/{roomPaymentId}")
    fun getByRoomPayment(
        @PathVariable roomPaymentId: UUID
    ): ResponseEntity<List<MemberPaymentResponseDTO>> {

        return ResponseEntity.ok(
            memberPaymentService.getByRoomPayment(
                roomPaymentId
            )
        )
    }

    @GetMapping("/payer/{payerUserId}")
    fun getByPayer(
        @PathVariable payerUserId: UUID
    ): ResponseEntity<List<MemberPaymentResponseDTO>> {

        return ResponseEntity.ok(
            memberPaymentService.getByPayer(
                payerUserId
            )
        )
    }

    @GetMapping("/receiver/{receiverUserId}")
    fun getByReceiver(
        @PathVariable receiverUserId: UUID
    ): ResponseEntity<List<MemberPaymentResponseDTO>> {

        return ResponseEntity.ok(
            memberPaymentService.getByReceiver(
                receiverUserId
            )
        )
    }

    @GetMapping("/status/{status}")
    fun getByStatus(
        @PathVariable status: String
    ): ResponseEntity<List<MemberPaymentResponseDTO>> {

        return ResponseEntity.ok(
            memberPaymentService.getByStatus(
                status.uppercase()
            )
        )
    }

    @PutMapping("/{id}/confirm")
    fun confirm(
        @PathVariable id: UUID
    ): ResponseEntity<MemberPaymentResponseDTO> {

        return ResponseEntity.ok(
            memberPaymentService.confirm(id)
        )
    }

    @PutMapping("/{id}/reject")
    fun reject(
        @PathVariable id: UUID
    ): ResponseEntity<MemberPaymentResponseDTO> {

        return ResponseEntity.ok(
            memberPaymentService.reject(id)
        )
    }

    @PutMapping("/{id}/cancel")
    fun cancel(
        @PathVariable id: UUID
    ): ResponseEntity<MemberPaymentResponseDTO> {

        return ResponseEntity.ok(
            memberPaymentService.cancel(id)
        )
    }
}