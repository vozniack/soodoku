package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.sse.SseEventDto
import dev.vozniack.soodoku.core.api.dto.sse.SseEventPayload
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

    val emitters: MutableMap<UUID, MutableList<SseEmitter>> = ConcurrentHashMap()

    fun subscribeSse(userId: UUID): SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE)
        val userEmitters = emitters.computeIfAbsent(userId) { CopyOnWriteArrayList() }

        userEmitters.add(emitter)
        emitter.registerCleanup(userEmitters)

        return emitter
    }

    fun sendSseEvent(userId: UUID, event: SseEventDto<out SseEventPayload>) {
        val userEmitters = emitters[userId] ?: return

        userEmitters.removeIf { emitter ->
            try {
                emitter.send(
                    SseEmitter.event()
                        .name(event.event.name)
                        .data(event.payload)
                )
                false
            } catch (ex: IOException) {
                true
            } catch (ex: Exception) {
                logger.warn { "Error sending SSE to user $userId: ${ex.message}" }
                true
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    fun sendHeartbeats() {
        emitters.values.forEach { userEmitters ->
            userEmitters.removeIf { emitter ->
                try {
                    emitter.send(SseEmitter.event().comment("heartbeat"))
                    false
                } catch (_: Exception) {
                    true
                }
            }
        }
    }

    private fun SseEmitter.registerCleanup(userEmitters: MutableList<SseEmitter>) {
        val remove = { userEmitters.remove(this) }

        onCompletion { remove() }
        onTimeout { remove() }
        onError { remove() }
    }

    companion object : KLogging()
}
