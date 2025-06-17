package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.mock.mockLoginRequest
import dev.vozniack.soodoku.core.mock.mockSignupRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AuthValidatorTest : AbstractUnitTest() {

    @Test
    fun `validate valid login request`() {
        mockLoginRequest().validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate login request with invalid email`(email: String) {
        assertThrows<BadRequestException>("Invalid email: '$email'") {
            mockLoginRequest(email = email).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate login request with invalid password`(password: String) {
        assertThrows<BadRequestException>("Invalid password: '$password'") {
            mockLoginRequest(password = password).validate()
        }
    }

    @Test
    fun `validate valid signup request`() {
        mockSignupRequest().validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "john.doesecuro.com", "john.doe@securo"])
    fun `validate signup request with invalid email format`(email: String) {
        assertThrows<BadRequestException>("Invalid email format: '$email'") {
            mockSignupRequest(email = email).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "password1!", "pAssword", "pAssword1", "P1!"])
    fun `validate signup request with invalid password`(password: String) {
        assertThrows<BadRequestException>("Invalid password: '$password'") {
            mockSignupRequest(password = password).validate()
        }
    }

    @Test
    fun `validate valid username`() {
        mockSignupRequest(username = "validUsername").validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate signup request with invalid username`(username: String) {
        assertThrows<BadRequestException>("Invalid username: '$username'") {
            mockSignupRequest(username = username).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["en_US", "pl_PL", "de_DE"])
    fun `validate signup request with valid language codes`(language: String) {
        mockSignupRequest(language = language).validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "en_us", "english_us", "en"])
    fun `validate signup request with invalid language`(language: String) {
        assertThrows<BadRequestException>("Invalid language: '$language'") {
            mockSignupRequest(language = language).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["dark", "light"])
    fun `validate signup request with valid themes`(theme: String) {
        mockSignupRequest(theme = theme).validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate signup request with invalid themes`(theme: String) {
        assertThrows<BadRequestException>("Invalid theme: '$theme'") {
            mockSignupRequest(theme = theme).validate()
        }
    }
}
