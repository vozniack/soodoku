package dev.vozniack.soodoku.core.api.dto

import java.util.UUID

data class UserDto(
    val id: UUID,

    val email: String,

    val username: String,
    val language: String,
    val theme: String
)

data class UserUsernameUpdateDto(
    val username: String
)

data class UserPasswordUpdateDto(
    val password: String
)

data class UserLanguageUpdateDto(
    val language: String
)

data class UserThemeUpdateDto(
    val theme: String
)
