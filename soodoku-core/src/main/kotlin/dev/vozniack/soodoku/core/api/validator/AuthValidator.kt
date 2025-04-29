package dev.vozniack.soodoku.core.api.validator

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.internal.exception.BadRequestException
import dev.vozniack.soodoku.core.util.throwIfTrue

fun LoginRequestDto.validate() {
    throwIfTrue(BadRequestException("Email can't be empty")) { email.isEmpty() }
    throwIfTrue(BadRequestException("Password can't be empty")) { password.isEmpty() }
}
