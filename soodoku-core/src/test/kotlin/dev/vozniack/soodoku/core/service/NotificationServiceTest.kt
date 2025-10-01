package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.sse.SseEventDto
import dev.vozniack.soodoku.core.fixture.mockFriendInvitation
import dev.vozniack.soodoku.core.fixture.mockUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class NotificationServiceTest @Autowired constructor(
    private val notificationService: NotificationService
) : AbstractUnitTest() {

    @BeforeEach
    fun clearEmitters() {
        notificationService.emitters.clear()
    }

    @Test
    fun `subscribeSse should create and register new emitter for user`() {
        val userId = UUID.randomUUID()

        val emitter = notificationService.subscribeSse(userId)

        assertNotNull(emitter)
        assertEquals(1, notificationService.emitters[userId]?.size)
        assertTrue(notificationService.emitters[userId]?.contains(emitter) == true)
    }

    @Test
    fun `subscribeSse should support multiple emitters for same user`() {
        val userId = UUID.randomUUID()

        val emitter1 = notificationService.subscribeSse(userId)
        val emitter2 = notificationService.subscribeSse(userId)
        val emitter3 = notificationService.subscribeSse(userId)

        assertEquals(3, notificationService.emitters[userId]?.size)
        assertEquals(listOf(emitter1, emitter2, emitter3), notificationService.emitters[userId])
    }

    @Test
    fun `subscribeSse should set timeout to Long MAX_VALUE`() {
        val userId = UUID.randomUUID()

        val emitter = notificationService.subscribeSse(userId)

        assertEquals(Long.MAX_VALUE, emitter.timeout)
    }

    @Test
    fun `subscribeSse should register completion callback`() {
        val userId = UUID.randomUUID()
        val emitter = notificationService.subscribeSse(userId)

        assertNotNull(emitter)
        assertEquals(1, notificationService.emitters[userId]?.size)
    }

    @Test
    fun `subscribeSse should register timeout callback`() {
        val userId = UUID.randomUUID()
        val emitter = notificationService.subscribeSse(userId)

        assertNotNull(emitter)
        assertEquals(1, notificationService.emitters[userId]?.size)
    }

    @Test
    fun `subscribeSse should register error callback`() {
        val userId = UUID.randomUUID()
        val emitter = notificationService.subscribeSse(userId)

        assertNotNull(emitter)
        assertEquals(1, notificationService.emitters[userId]?.size)
    }

    @Test
    fun `sendSseEvent should send event to all user emitters`() {
        val userId = UUID.randomUUID()
        val emitter1 = notificationService.subscribeSse(userId)
        val emitter2 = notificationService.subscribeSse(userId)

        val event = SseEventDto.friendInvitationReceived(
            mockFriendInvitation(sender = mockUser(username = "testuser"))
        )

        notificationService.sendSseEvent(userId, event)

        assertEquals(2, notificationService.emitters[userId]?.size)
        assertTrue(notificationService.emitters[userId]?.contains(emitter1) == true)
        assertTrue(notificationService.emitters[userId]?.contains(emitter2) == true)
    }

    @Test
    fun `sendSseEvent should do nothing when user has no emitters`() {
        val userId = UUID.randomUUID()
        val event = SseEventDto.friendInvitationReceived(
            mockFriendInvitation(sender = mockUser(username = "testuser"))
        )

        notificationService.sendSseEvent(userId, event)

        assertNull(notificationService.emitters[userId])
    }

    @Test
    fun `sendSseEvent should remove emitter on IOException`() {
        val userId = UUID.randomUUID()
        val emitter = MockFailingEmitter(IOException("Connection closed"))

        notificationService.emitters.computeIfAbsent(userId) { mutableListOf() }.add(emitter)

        val event = SseEventDto.friendInvitationReceived(
            mockFriendInvitation(sender = mockUser(username = "testuser"))
        )

        notificationService.sendSseEvent(userId, event)

        assertTrue(notificationService.emitters[userId]?.isEmpty() == true)
    }

    @Test
    fun `sendSseEvent should remove emitter on general exception`() {
        val userId = UUID.randomUUID()
        val emitter = MockFailingEmitter(RuntimeException("Unexpected error"))

        notificationService.emitters.computeIfAbsent(userId) { mutableListOf() }.add(emitter)

        val event = SseEventDto.friendInvitationReceived(
            mockFriendInvitation(sender = mockUser(username = "testuser"))
        )

        notificationService.sendSseEvent(userId, event)

        assertTrue(notificationService.emitters[userId]?.isEmpty() == true)
    }

    @Test
    fun `sendSseEvent should keep working emitters and remove failing ones`() {
        val userId = UUID.randomUUID()
        val workingEmitter = notificationService.subscribeSse(userId)
        val failingEmitter = MockFailingEmitter(IOException("Connection closed"))

        notificationService.emitters[userId]?.add(failingEmitter)

        val event = SseEventDto.friendInvitationReceived(
            mockFriendInvitation(sender = mockUser(username = "testuser"))
        )

        notificationService.sendSseEvent(userId, event)

        assertEquals(1, notificationService.emitters[userId]?.size)
        assertTrue(notificationService.emitters[userId]?.contains(workingEmitter) == true)
        assertFalse(notificationService.emitters[userId]?.contains(failingEmitter) == true)
    }

    @Test
    fun `sendHeartbeats should send heartbeat to all emitters`() {
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()

        notificationService.subscribeSse(userId1)
        notificationService.subscribeSse(userId2)

        notificationService.sendHeartbeats()

        assertEquals(1, notificationService.emitters[userId1]?.size)
        assertEquals(1, notificationService.emitters[userId2]?.size)
    }

    @Test
    fun `sendHeartbeats should remove failing emitters`() {
        val userId = UUID.randomUUID()
        val workingEmitter = notificationService.subscribeSse(userId)
        val failingEmitter = MockFailingEmitter(IOException("Connection closed"))

        notificationService.emitters[userId]?.add(failingEmitter)
        notificationService.sendHeartbeats()

        assertEquals(1, notificationService.emitters[userId]?.size)
        assertTrue(notificationService.emitters[userId]?.contains(workingEmitter) == true)
        assertFalse(notificationService.emitters[userId]?.contains(failingEmitter) == true)
    }

    @Test
    fun `sendHeartbeats should handle multiple users`() {
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()
        val userId3 = UUID.randomUUID()

        notificationService.subscribeSse(userId1)
        notificationService.subscribeSse(userId2)
        notificationService.subscribeSse(userId2)

        val failingEmitter = MockFailingEmitter(RuntimeException("Error"))

        notificationService.emitters.computeIfAbsent(userId3) { mutableListOf() }.add(failingEmitter)
        notificationService.sendHeartbeats()

        assertEquals(1, notificationService.emitters[userId1]?.size)
        assertEquals(2, notificationService.emitters[userId2]?.size)
        assertTrue(notificationService.emitters[userId3]?.isEmpty() == true)
    }

    private class MockFailingEmitter(private val exception: Exception) : SseEmitter() {
        override fun send(event: SseEventBuilder) {
            throw exception
        }

        override fun send(obj: Any) {
            throw exception
        }
    }
}
