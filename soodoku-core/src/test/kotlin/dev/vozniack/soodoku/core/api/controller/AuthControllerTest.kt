package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.AuthResponseDto
import dev.vozniack.soodoku.core.api.dto.RefreshRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.mock.mockLoginRequest
import dev.vozniack.soodoku.core.mock.mockRefreshRequest
import dev.vozniack.soodoku.core.mock.mockSignupRequest
import dev.vozniack.soodoku.core.mock.mockUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class AuthControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    private val objectMapper = jacksonObjectMapper()

    @AfterEach
    fun `clean up`() {
        userRepository.deleteAll()
    }

    @Test
    fun `login user`() {
        userRepository.save(mockUser().apply { password = passwordEncoder.encode("J0hn123!") })

        val request: LoginRequestDto = mockLoginRequest(password = "J0hn123!")

        val response: AuthResponseDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)
    }

    @Test
    fun `signup user`() {
        val request: SignupRequestDto = mockSignupRequest()

        val response: AuthResponseDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)
    }

    @Test
    fun `refresh user`() {
        userRepository.save(mockUser())

        val request: RefreshRequestDto = mockRefreshRequest()

        val response: AuthResponseDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)
    }
}
