package user.profile.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerController {
    @GetMapping("/")
    fun serverRunning(): String {
        return "server running..."
    }
}