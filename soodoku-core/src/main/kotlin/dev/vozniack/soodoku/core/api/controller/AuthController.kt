package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.LoginRequestDto
import dev.vozniack.soodoku.core.api.dto.LoginResponseDto
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
    fun login(@RequestBody request: LoginRequestDto): LoginResponseDto {
        request.validate().also {
            logger.debug { "Logging in user ${request.email}" }
        }

        return authService.login(request)
    }

    companion object : KLogging()
}
