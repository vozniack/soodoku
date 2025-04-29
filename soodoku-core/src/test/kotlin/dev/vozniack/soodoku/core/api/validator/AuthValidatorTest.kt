package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.mock.mockLoginRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthValidatorTest : AbstractUnitTest() {

    @Test
    fun `validate login request`() {
        mockLoginRequest().validate()

        // missing email
        assertThrows<BadRequestException> {
            mockLoginRequest(email = "").validate()
        }

        // missing password
        assertThrows<BadRequestException> {
            mockLoginRequest(password = "").validate()
        }
    }
}
