package dev.vozniack.soodoku.core.api.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class SignupRequestDto(
    val email: String,
    val password: String,
    val username: String
)

data class AuthResponseDto(
    val token: String
)
