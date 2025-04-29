package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.LoginResponseDto
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.mock.mockLoginRequest
import dev.vozniack.soodoku.core.mock.mockUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest @Autowired constructor(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up`() {
        userRepository.deleteAll()
    }

    @Test
    fun `login user`() {
        userRepository.save(mockUser().apply { password = passwordEncoder.encode("J0hn123!") })
        val request: LoginRequestDto = mockLoginRequest(password = "J0hn123!")

        val response: LoginResponseDto = authService.login(request)
        assertNotNull(response.token)
    }

    @Test
    fun `login with incorrect password`() {
        userRepository.save(mockUser().apply { password = passwordEncoder.encode("J0hn123!") })
        val request: LoginRequestDto = mockLoginRequest(password = "BlaBla123!")

        assertThrows<UnauthorizedException> {
            authService.login(request)
        }
    }
}
