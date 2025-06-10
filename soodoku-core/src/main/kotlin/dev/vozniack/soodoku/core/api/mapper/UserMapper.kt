package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.domain.entity.User

fun User.toDto(): UserDto = UserDto(
    id = id,
    email = email,
    username = username,
)
