package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.api.dto.UserLanguageUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserPasswordUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserThemeUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserUsernameUpdateDto
import dev.vozniack.soodoku.core.domain.entity.User

fun mockUser(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!",
    username: String = "johndoe",
    language: String = "en_EN",
    theme: String = "light"
): User = User(
    email = email,
    password = password,
    username = username,
    language = language,
    theme = theme
)

fun mockUserUsernameUpdateDto(username: String = "johnDoe123"): UserUsernameUpdateDto = UserUsernameUpdateDto(username)

fun mockUserPasswordUpdateDto(password: String = "J0hn456!"): UserPasswordUpdateDto = UserPasswordUpdateDto(password)

fun mockUserLanguageUpdateDto(language: String = "pl_PL"): UserLanguageUpdateDto = UserLanguageUpdateDto(language)

fun mockUserThemeUpdateDto(theme: String = "dark"): UserThemeUpdateDto = UserThemeUpdateDto(theme)
