package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.core.fixture.mockUserLanguageUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserPasswordUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserThemeUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserUsernameUpdateDto
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class UserControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    private val objectMapper = jacksonObjectMapper()

    @AfterEach
    fun `clean up`() {
        userRepository.deleteAll()
    }

    @Test
    fun `get currently logged user with logged user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val response: UserDto = objectMapper.readValue(
            mockMvc.perform(
                get("/api/users").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
    }

    @Test
    fun `get currently logged user with not logged user`() {
        mockMvc.perform(
            get("/api/users").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update username with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserUsernameUpdateDto("newUsername")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update username with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserUsernameUpdateDto("newUsername")

        mockMvc.perform(
            put("/api/users/${user.id}/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update password with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserPasswordUpdateDto("NewP@ssw0rd")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update password with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val dto = mockUserPasswordUpdateDto("NewP@ssw0rd")

        mockMvc.perform(
            put("/api/users/${user.id}/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update language with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserLanguageUpdateDto("pl_PL")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update language with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserLanguageUpdateDto("pl_PL")

        mockMvc.perform(
            put("/api/users/${user.id}/language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update theme with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserThemeUpdateDto("dark")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update theme with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserThemeUpdateDto("dark")

        mockMvc.perform(
            put("/api/users/${user.id}/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }
}
