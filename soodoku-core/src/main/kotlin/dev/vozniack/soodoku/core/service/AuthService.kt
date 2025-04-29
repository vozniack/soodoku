package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.LoginResponseDto
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.config.JwtConfig
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtConfig: JwtConfig,
) {

    fun login(request: LoginRequestDto): LoginResponseDto = userRepository.findByEmail(request.email)
        ?.takeIf { passwordEncoder.matches(request.password, it.password) }
        ?.let { LoginResponseDto(token = buildToken(it)) }
        ?: throw UnauthorizedException("User ${request.email} has not been authorized")

    private fun buildToken(user: User): String = Jwts.builder()
        .subject(user.email)
        .expiration(Date(Date().time + jwtConfig.expiration.toInt()))
        .signWith(Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8)))
        .compact()
}
