package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.api.dto.UserLanguageUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserPasswordUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserThemeUpdateDto
import dev.vozniack.soodoku.core.api.dto.UserUsernameUpdateDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.api.validator.validate
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.internal.logging.KLogging
import dev.vozniack.soodoku.core.service.UserService
import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getCurrentlyLoggedUser(): UserDto = userService.currentlyLoggedUser()?.toDto()
        ?: throw UnauthorizedException("You don't have access to this resource")

    @PutMapping("/{id}/username")
    fun updateUsername(@PathVariable id: UUID, @RequestBody request: UserUsernameUpdateDto): UserDto {
        request.validate().also {
            logger.debug { "Updating username for user $id" }
        }

        return userService.updateUsername(request)
    }

    @PutMapping("/{id}/password")
    fun updatePassword(@PathVariable id: UUID, @RequestBody request: UserPasswordUpdateDto): UserDto {
        request.validate().also {
            logger.debug { "Updating password for user $id" }
        }

        return userService.updatePassword(request)
    }

    @PutMapping("/{id}/language")
    fun updateLanguage(@PathVariable id: UUID, @RequestBody request: UserLanguageUpdateDto): UserDto {
        request.validate().also {
            logger.debug { "Updating language for user $id" }
        }

        return userService.updateLanguage(request)
    }

    @PutMapping("/{id}/theme")
    fun updateTheme(@PathVariable id: UUID, @RequestBody request: UserThemeUpdateDto): UserDto {
        request.validate().also {
            logger.debug { "Updating theme for user $id" }
        }

        return userService.updateTheme(request)
    }

    companion object : KLogging()
}
