package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.AuthResponseDto
import dev.vozniack.soodoku.core.api.dto.RefreshRequestDto
import dev.vozniack.soodoku.core.api.dto.SignupRequestDto
import dev.vozniack.soodoku.core.api.validator.validate
import dev.vozniack.soodoku.core.internal.logging.KLogging
import dev.vozniack.soodoku.core.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDto): AuthResponseDto {
        request.validate().also {
            logger.debug { "Logging in user ${request.email}" }
        }

        return authService.login(request)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequestDto): AuthResponseDto {
        request.validate().also {
            logger.debug { "Signing up user ${request.email}" }
        }

        return authService.signup(request)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequestDto): AuthResponseDto {
        request.validate().also {
            logger.debug { "Refreshing token" }
        }

        return authService.refresh(request)
    }

    companion object : KLogging()
}
