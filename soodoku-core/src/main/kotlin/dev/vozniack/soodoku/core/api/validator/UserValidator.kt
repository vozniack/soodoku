package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.api.dto.UserLanguageUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserPasswordUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserThemeUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserUsernameUpdateDto
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.util.matchesLanguageRegex
import dev.vozniack.soodoku.core.util.matchesPasswordRegex
import dev.vozniack.soodoku.core.util.throwIfTrue

fun UserUsernameUpdateDto.validate() {
    throwIfTrue(BadRequestException("Email must not be empty")) { username.isBlank() }
}

fun UserPasswordUpdateDto.validate() {
    throwIfTrue(BadRequestException("Password must not be empty")) { password.isBlank() }

    throwIfTrue(BadRequestException("Password must be 6 characters length and contain capital letter, number and special character")) {
        !password.matchesPasswordRegex()
    }
}

fun UserLanguageUpdateDto.validate() {
    throwIfTrue(BadRequestException("Language must not be empty")) { language.isBlank() }

    throwIfTrue(BadRequestException("Language must be in ISO form, e.g. en_US")) {
        !language.matchesLanguageRegex()
    }
}

fun UserThemeUpdateDto.validate() {
    throwIfTrue(BadRequestException("Theme must not be empty")) { theme.isBlank() }
}
