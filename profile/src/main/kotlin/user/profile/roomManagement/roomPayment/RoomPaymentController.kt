package user.profile.roomManagement.roomPayment

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import user.profile.roomManagement.roomPayment.roomPaymentDTO.RoomPaymentRequestDTO
import user.profile.roomManagement.roomPayment.roomPaymentDTO.RoomPaymentResponseDTO
import java.util.UUID

@RestController
@RequestMapping("/api/room-payment")
class RoomPaymentController(
    private val roomPaymentService: RoomPaymentService
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: RoomPaymentRequestDTO
    ): ResponseEntity<RoomPaymentResponseDTO> {

        val response = roomPaymentService.create(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: UUID
    ): ResponseEntity<RoomPaymentResponseDTO> {

        return ResponseEntity.ok(
            roomPaymentService.getById(id)
        )
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<RoomPaymentResponseDTO>> {

        return ResponseEntity.ok(
            roomPaymentService.getAll()
        )
    }

    @GetMapping("/list")
    fun getList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<RoomPaymentResponseDTO>> {

        return ResponseEntity.ok(
            roomPaymentService.getList(page, size)
        )
    }

    @GetMapping("/room/{roomId}")
    fun getByRoom(
        @PathVariable roomId: UUID
    ): ResponseEntity<List<RoomPaymentResponseDTO>> {

        return ResponseEntity.ok(
            roomPaymentService.getByRoom(roomId)
        )
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: RoomPaymentRequestDTO
    ): ResponseEntity<RoomPaymentResponseDTO> {

        return ResponseEntity.ok(
            roomPaymentService.update(id, request)
        )
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {

        roomPaymentService.delete(id)

        return ResponseEntity.noContent().build()
    }
}