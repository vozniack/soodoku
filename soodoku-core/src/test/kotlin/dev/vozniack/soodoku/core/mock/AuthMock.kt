package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto

fun mockLoginRequest(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!",
): LoginRequestDto = LoginRequestDto(email = email, password = password)
