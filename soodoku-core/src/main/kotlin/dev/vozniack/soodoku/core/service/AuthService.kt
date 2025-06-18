package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.AuthResponseDto
import dev.vozniack.soodoku.core.api.dto.RefreshRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.api.mapper.toUserWithEncodedPassword
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.config.JwtConfig
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.internal.logging.KLogging
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

    fun login(request: LoginRequestDto): AuthResponseDto = userRepository.findByEmail(request.email)
        ?.takeIf { passwordEncoder.matches(request.password, it.password) }
        ?.let {
            AuthResponseDto(
                accessToken = buildToken(it, jwtConfig.accessTokenExpiration.toInt()),
                refreshToken = buildToken(it, jwtConfig.refreshTokenExpiration.toInt())
            )
        } ?: throw UnauthorizedException("You don't have access to this resource")

    fun signup(request: SignupRequestDto): AuthResponseDto {
        userRepository.findByEmail(request.email)?.let {
            throw ConflictException("User with email ${request.email} already exists")
        }

        return userRepository.save(request toUserWithEncodedPassword passwordEncoder.encode(request.password)).let {
            AuthResponseDto(
                accessToken = buildToken(it, jwtConfig.accessTokenExpiration.toInt()),
                refreshToken = buildToken(it, jwtConfig.refreshTokenExpiration.toInt())
            )
        }
    }

    fun refresh(request: RefreshRequestDto): AuthResponseDto =
        userRepository.findByEmail(parseRefreshToken(request.refreshToken))?.let {
            AuthResponseDto(
                accessToken = buildToken(it, jwtConfig.accessTokenExpiration.toInt()),
                refreshToken = buildToken(it, jwtConfig.refreshTokenExpiration.toInt())
            )
        } ?: throw UnauthorizedException("You don't have access to this resource")

    private fun buildToken(user: User, expiration: Int): String = Jwts.builder()
        .subject(user.email)
        .expiration(Date(System.currentTimeMillis() + expiration))
        .signWith(Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8)))
        .compact()

    private fun parseRefreshToken(token: String): String = try {
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))).build()
            .parseSignedClaims(token).takeIf { !it.payload.expiration.before(Date()) }?.payload?.subject
            ?: throw UnauthorizedException("You don't have access to this resource")
    } catch (exception: Exception) {
        logger.warn { exception.message }

        throw UnauthorizedException("You don't have access to this resource")
    }

    companion object : KLogging()
}
