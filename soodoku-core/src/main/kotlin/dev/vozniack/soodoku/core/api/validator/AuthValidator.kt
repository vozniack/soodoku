package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.util.matchesEmailRegex
import dev.vozniack.soodoku.core.util.matchesLanguageRegex
import dev.vozniack.soodoku.core.util.matchesPasswordRegex
import dev.vozniack.soodoku.core.util.throwIfTrue

fun LoginRequestDto.validate() {
    throwIfTrue(BadRequestException("Email must not be empty")) { email.isBlank() }
    throwIfTrue(BadRequestException("Password must not be empty")) { password.isBlank() }
}

fun SignupRequestDto.validate() {
    throwIfTrue(BadRequestException("Email must not be empty")) { email.isBlank() }

    throwIfTrue(BadRequestException("Email must be a valid address")) {
        !email.matchesEmailRegex()
    }

    throwIfTrue(BadRequestException("Password must not be empty")) { password.isBlank() }

    throwIfTrue(BadRequestException("Password must be 6 characters length and contain capital letter, number and special character")) {
        !password.matchesPasswordRegex()
    }

    throwIfTrue(BadRequestException("Username must not be empty")) { username.isBlank() }

    throwIfTrue(BadRequestException("Language must not be empty")) { language.isBlank() }

    throwIfTrue(BadRequestException("Language must be in ISO form, e.g. en_US")) {
        !language.matchesLanguageRegex()
    }

    throwIfTrue(BadRequestException("Theme must not be empty")) { theme.isBlank() }
}
