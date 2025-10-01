package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.service.NotificationService
import dev.vozniack.soodoku.core.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    @GetMapping("/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun sse(): SseEmitter {
        val userId = userService.currentlyLoggedUser()?.id
            ?: throw UnauthorizedException("You don't have access to this resource")

        return notificationService.subscribeSse(userId)
    }
}
