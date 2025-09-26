package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.AuthResponseDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.fixture.mockLoginRequest
import dev.vozniack.soodoku.core.fixture.mockRefreshRequest
import dev.vozniack.soodoku.core.fixture.mockSignupRequest
import dev.vozniack.soodoku.core.fixture.mockUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
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

        val response: AuthResponseDto = authService.login(request)
        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)
    }

    @Test
    fun `login with incorrect password`() {
        userRepository.save(mockUser().apply { password = passwordEncoder.encode("J0hn123!") })
        val request: LoginRequestDto = mockLoginRequest(password = "BlaBla123!")

        assertThrows<UnauthorizedException> {
            authService.login(request)
        }
    }

    @Test
    fun `signup user`() {
        val request: SignupRequestDto = mockSignupRequest()
        val response: AuthResponseDto = authService.signup(request)

        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)

        assertEquals(1, userRepository.count())
    }

    @Test
    fun `signup if user already exists`() {
        userRepository.save(mockUser(email = "john.doe@soodoku.com"))

        val request: SignupRequestDto = mockSignupRequest(email = "john.doe@soodoku.com")

        assertThrows<ConflictException> {
            authService.signup(request)
        }
    }

    @Test
    fun `refresh when user exists`() {
        val user = userRepository.save(mockUser(email = "john.doe@soodoku.com"))

        val request = mockRefreshRequest(email = user.email)
        val response = authService.refresh(request)

        assertNotNull(response.accessToken)
        assertNotNull(response.refreshToken)
    }

    @Test
    fun `refresh when refresh token is expired`() {
        val user = userRepository.save(mockUser(email = "john.doe@soodoku.com"))

        val request = mockRefreshRequest(email = user.email, expiration = -128)

        assertThrows<UnauthorizedException> {
            authService.refresh(request)
        }
    }

    @Test
    fun `refresh when refresh token is incorrect`() {
        userRepository.save(mockUser(email = "john.doe@soodoku.com"))

        val request = mockRefreshRequest(refreshToken = "1234abcd")

        assertThrows<UnauthorizedException> {
            authService.refresh(request)
        }
    }
}
