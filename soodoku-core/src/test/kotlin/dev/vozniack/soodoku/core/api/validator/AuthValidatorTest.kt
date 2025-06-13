package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.mock.mockLoginRequest
import dev.vozniack.soodoku.core.mock.mockSignupRequest
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

    @Test
    fun `validate signup request`() {
        mockSignupRequest().validate()

        // missing email
        assertThrows<BadRequestException> {
            mockSignupRequest(email = "").validate()
        }

        // missing password
        assertThrows<BadRequestException> {
            mockSignupRequest(password = "").validate()
        }

        // email without @ character
        assertThrows<BadRequestException> {
            mockSignupRequest(email = "john.doesecuro.com").validate()
        }

        // email without domain
        assertThrows<BadRequestException> {
            mockSignupRequest(email = "john.doe@securo").validate()
        }

        // password without capital letter
        assertThrows<BadRequestException> {
            mockSignupRequest(password = "password1!").validate()
        }

        // password without number
        assertThrows<BadRequestException> {
            mockSignupRequest(password = "pAssword!").validate()
        }

        // password without special character
        assertThrows<BadRequestException> {
            mockSignupRequest(password = "pAssword!").validate()
        }
    }
}
