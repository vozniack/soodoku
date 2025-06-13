package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.util.matchesEmailRegex
import dev.vozniack.soodoku.core.util.matchesPasswordRegex
import dev.vozniack.soodoku.core.util.throwIfTrue

fun LoginRequestDto.validate() {
    throwIfTrue(BadRequestException("Email can't be empty")) { email.isEmpty() }
    throwIfTrue(BadRequestException("Password can't be empty")) { password.isEmpty() }
}

fun SignupRequestDto.validate() {
    throwIfTrue(BadRequestException("Email can't be empty")) { email.isEmpty() }
    throwIfTrue(BadRequestException("Password can't be empty")) { password.isEmpty() }

    throwIfTrue(BadRequestException("Email must be a valid address")) {
        !email.matchesEmailRegex()
    }

    throwIfTrue(BadRequestException("Password must be 6 characters length and contain capital letter, number and special character")) {
        !password.matchesPasswordRegex()
    }
}
