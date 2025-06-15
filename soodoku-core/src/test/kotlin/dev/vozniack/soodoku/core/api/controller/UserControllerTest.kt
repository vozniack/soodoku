package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.mock.mockUser
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class UserControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    @BeforeEach
    fun `clear up before`() {
        userRepository.deleteAll()
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun `clear up after`() {
        userRepository.deleteAll()
    }

    @Test
    fun `get currently logged user with anonymous user`() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "anonymousUser", null, emptyList()
        )

        mockMvc.perform(
            get("/api/users").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get currently logged user with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val response: UserDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                get("/api/users").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
    }

    @Test
    fun `get currently logged user with existing but not logged in user`() {
        userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            get("/api/users").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }
}
