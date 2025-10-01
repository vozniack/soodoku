package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.core.service.NotificationService
import kotlin.test.Ignore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class NotificationControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val notificationService: NotificationService,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    @AfterEach
    fun `clean up`() {
        userRepository.deleteAll()
        notificationService.emitters.clear()
    }

    @Test
    fun `stream should return SSE emitter for authenticated user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val result = mockMvc.perform(
            get("/api/notifications/sse").accept(MediaType.TEXT_EVENT_STREAM_VALUE)
        ).andExpect(status().isOk)
            .andExpect(request().asyncStarted())
            .andReturn()

        assertNotNull(result.request.asyncContext)
        assertNotNull(notificationService.emitters[user.id])
    }

    @Test
    @Ignore // #todo remove after improving security layer
    fun `stream should return 401 for unauthenticated user`() {
        mockMvc.perform(
            get("/api/notifications/sse").accept(MediaType.TEXT_EVENT_STREAM_VALUE)
        ).andExpect(status().isUnauthorized)
    }
}
