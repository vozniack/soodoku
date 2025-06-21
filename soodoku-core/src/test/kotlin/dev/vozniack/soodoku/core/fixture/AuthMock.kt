package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.RefreshRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date

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

fun mockRefreshRequest(
    refreshToken: String
): RefreshRequestDto = RefreshRequestDto(
    refreshToken = refreshToken,
)

fun mockRefreshRequest(
    email: String = "john.doe@soodoku.com",
    expiration: Int = 75000
): RefreshRequestDto = RefreshRequestDto(
    refreshToken = Jwts.builder()
        .subject(email)
        .expiration(Date(System.currentTimeMillis() + expiration))
        .signWith(hmacShaKey())
        .compact()
)

private fun hmacShaKey() = Keys.hmacShaKeyFor(
    "656uzf9s3JhPGu1HSj6nkhV8-Jc-TH43C5Wd5p_BH9Zfbq03n4hWMFlskVP6_tZScNWBOmUAmNxgPVTuOmv6lw"
        .toByteArray(StandardCharsets.UTF_8)
)

