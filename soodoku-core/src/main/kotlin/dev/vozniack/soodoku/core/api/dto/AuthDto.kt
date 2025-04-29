package dev.vozniack.soodoku.core.api.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val token: String
)
