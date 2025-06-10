package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getCurrentlyLoggedUser(): UserDto = userService.currentlyLoggedUser()?.toDto()
        ?: throw UnauthorizedException("You don't have access")
}
