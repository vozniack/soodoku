package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto

fun mockLoginRequest(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!",
): LoginRequestDto = LoginRequestDto(email = email, password = password)

fun mockSignupRequest(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!",
    username: String = "johndoe",
    language: String = "en_EN",
    theme: String = "light"
): SignupRequestDto = SignupRequestDto(
    email = email,
    password = password,
    username = username,
    language = language,
    theme = theme
)
