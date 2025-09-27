package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.SseEventDto
import dev.vozniack.soodoku.core.internal.logging.KLogging
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Service
class NotificationService {

    private val emitters: MutableMap<UUID, MutableList<SseEmitter>> = ConcurrentHashMap()

    fun subscribeSse(userId: UUID): SseEmitter {
        val emitter = SseEmitter(0L)
        val userEmitters = emitters.computeIfAbsent(userId) { CopyOnWriteArrayList() }

        userEmitters.add(emitter)

        emitter.onCompletion { userEmitters.remove(emitter) }
        emitter.onTimeout { userEmitters.remove(emitter) }
        emitter.onError { userEmitters.remove(emitter) }

        return emitter
    }

    fun sendSseEvent(userId: UUID, event: SseEventDto<Any>) {
        val userEmitters = emitters[userId] ?: return

        val disconnectedEmitters = mutableListOf<SseEmitter>()

        userEmitters.forEach { emitter ->
            try {
                emitter.send(SseEmitter.event().name(event.type.name).data(event.payload))
            } catch (exception: IOException) {
                disconnectedEmitters += emitter
            } catch (exception: Exception) {
                disconnectedEmitters += emitter
                logger.warn { "Error sending SSE to user $userId: ${exception.message}" }
            }
        }

        if (disconnectedEmitters.isNotEmpty()) {
            userEmitters.removeAll(disconnectedEmitters)
        }
    }

    @Scheduled(fixedRate = 15000)
    fun sendHeartbeats() {
        emitters.values.flatten().forEach { emitter ->
            try {
                emitter.send(":heartbeat\n\n")
            } catch (exception: Exception) {
                logger.warn { exception }
            }
        }
    }

    companion object : KLogging()
}
