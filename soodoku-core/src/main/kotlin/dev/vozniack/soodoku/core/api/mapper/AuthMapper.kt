package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.domain.entity.User

infix fun SignupRequestDto.toUserWithEncodedPassword(password: String): User = User(
    email = email,
    password = password,
    username = username,
)
