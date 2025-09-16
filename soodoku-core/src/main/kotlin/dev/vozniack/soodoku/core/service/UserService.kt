package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.api.dto.UserLanguageUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserPasswordUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserThemeUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserUsernameUpdateDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun currentlyLoggedUser(): User? = SecurityContextHolder.getContext().authentication
        ?.takeIf { it.name != "anonymousUser" }
        ?.let { userRepository.findByEmail(it.name) ?: throw UnauthorizedException("Not found user with email $it") }

    fun updateUsername(request: UserUsernameUpdateDto): UserDto = currentlyLoggedUser()?.let {
        userRepository.save(it.apply { username = request.username })
    }?.toDto() ?: throw UnauthorizedException("You don't have access to this resource")

    fun updatePassword(request: UserPasswordUpdateDto): UserDto = currentlyLoggedUser()?.let {
        userRepository.save(it.apply { password = passwordEncoder.encode(request.password) })
    }?.toDto() ?: throw UnauthorizedException("You don't have access to this resource")

    fun updateLanguage(request: UserLanguageUpdateDto): UserDto = currentlyLoggedUser()?.let {
        userRepository.save(it.apply { language = request.language })
    }?.toDto() ?: throw UnauthorizedException("You don't have access to this resource")

    fun updateTheme(request: UserThemeUpdateDto): UserDto = currentlyLoggedUser()?.let {
        userRepository.save(it.apply { theme = request.theme })
    }?.toDto() ?: throw UnauthorizedException("You don't have access to this resource")
}
