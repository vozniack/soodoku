package dev.vozniack.soodoku.core.api.dto

import java.util.UUID

data class UserDto(
    val id: UUID,

    val email: String,
    val username: String?,
)
