package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.fixture.mockUserLanguageUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserPasswordUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserThemeUpdateDto
import dev.vozniack.soodoku.core.fixture.mockUserUsernameUpdateDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserValidatorTest : AbstractUnitTest() {

    @Test
    fun `validate valid username update request`() {
        mockUserUsernameUpdateDto("validUsername").validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate invalid username update request`(username: String) {
        assertThrows<BadRequestException> {
            mockUserUsernameUpdateDto(username).validate()
        }
    }

    @Test
    fun `validate valid password update request`() {
        mockUserPasswordUpdateDto("Valid1!").validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "password1!", "pAssword", "pAssword1", "P1!"])
    fun `validate invalid password update request`(password: String) {
        assertThrows<BadRequestException> {
            mockUserPasswordUpdateDto(password).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["en_US", "pl_PL", "de_DE"])
    fun `validate valid language update request`(language: String) {
        mockUserLanguageUpdateDto(language).validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "en_us", "english_us", "en"])
    fun `validate invalid language update request`(language: String) {
        assertThrows<BadRequestException> {
            mockUserLanguageUpdateDto(language).validate()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["dark", "light"])
    fun `validate valid theme update request`(theme: String) {
        mockUserThemeUpdateDto(theme).validate()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `validate invalid theme update request`(theme: String) {
        assertThrows<BadRequestException> {
            mockUserThemeUpdateDto(theme).validate()
        }
    }
}
