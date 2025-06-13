package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.mock.mockSignupRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class AuthMapperTest {

    @Test
    fun `map signup request to user`() {
        val request: SignupRequestDto = mockSignupRequest()
        val user: User = request toUserWithEncodedPassword "1234abcdItsEncoded"

        assertEquals(request.email, user.email)
        assertEquals(request.username, user.username)

        assertNotEquals(request.password, user.password)
        assertEquals("1234abcdItsEncoded", user.password)
    }
}
