package dev.vozniack.soodoku.core.api.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class SignupRequestDto(
    val email: String,
    val password: String,

    val username: String,
    val language: String,
    val theme: String
)

data class RefreshRequestDto(
    val refreshToken: String
)

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String
)
