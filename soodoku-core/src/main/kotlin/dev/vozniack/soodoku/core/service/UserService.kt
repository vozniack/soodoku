package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun currentlyLoggedUser(): User? = SecurityContextHolder.getContext().authentication
        ?.takeIf { it.name != "anonymousUser" }
        ?.let { userRepository.findByEmail(it.name) ?: throw UnauthorizedException("Not found user with email $it") }
}
