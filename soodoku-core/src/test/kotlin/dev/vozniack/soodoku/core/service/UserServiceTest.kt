package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.core.mock.mockUserLanguageUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserPasswordUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserThemeUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserUsernameUpdateDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @BeforeEach
    fun `clear up before`() {
        userRepository.deleteAll()
    }

    @AfterEach
    fun `clear up after`() {
        userRepository.deleteAll()
    }

    @Test
    fun `get empty currently logged user`() {
        assertNull(userService.currentlyLoggedUser())
    }

    @Test
    fun `get anonymous currently logged user`() {
        authenticate("anonymousUser")

        assertNull(userService.currentlyLoggedUser())
    }

    @Test
    fun `get currently logged user`() {
        val user = userRepository.save(mockUser())

        authenticate(user.email)

        val result = userService.currentlyLoggedUser()

        assertNotNull(result)
        assertEquals(user.id, result!!.id)
        assertEquals(user.email, result.email)
    }

    @Test
    fun `get not existing currently logged user`() {
        authenticate("jane.doe@soodoku.com")

        val exception = assertThrows<UnauthorizedException> {
            userService.currentlyLoggedUser()
        }

        assertTrue(exception.message!!.contains("jane.doe@soodoku.com"))
    }

    @Test
    fun `update username`() {
        val user = userRepository.save(mockUser())
        val request = mockUserUsernameUpdateDto()

        authenticate(user.email)

        userService.updateUsername(request)

        val fetchedUser = userRepository.findById(user.id).get()

        assertEquals(request.username, fetchedUser.username)
    }

    @Test
    fun `update password`() {
        val user = userRepository.save(mockUser())
        val request = mockUserPasswordUpdateDto()

        authenticate(user.email)

        userService.updatePassword(request)

        val fetchedUser = userRepository.findById(user.id).get()

        assertNotEquals(user.password, fetchedUser.password)
        assertNotEquals(request.password, fetchedUser.password)
    }

    @Test
    fun `update language`() {
        val user = userRepository.save(mockUser())
        val request = mockUserLanguageUpdateDto()

        authenticate(user.email)

        userService.updateLanguage(request)

        val fetchedUser = userRepository.findById(user.id).get()

        assertEquals(request.language, fetchedUser.language)
    }

    @Test
    fun `update theme`() {
        val user = userRepository.save(mockUser())
        val request = mockUserThemeUpdateDto()

        authenticate(user.email)

        userService.updateTheme(request)

        val fetchedUser = userRepository.findById(user.id).get()

        assertEquals(request.theme, fetchedUser.theme)
    }
}
