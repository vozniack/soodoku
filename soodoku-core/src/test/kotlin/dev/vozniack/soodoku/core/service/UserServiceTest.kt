package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.mock.mockUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @BeforeEach
    fun `clear up`() {
        userRepository.deleteAll()
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get empty currently logged user`() {
        assertNull(userService.currentlyLoggedUser())
    }

    @Test
    fun `get anonymous currently logged user`() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "anonymousUser", null, emptyList()
        )

        assertNull(userService.currentlyLoggedUser())
    }

    @Test
    fun `get currently logged user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val result = userService.currentlyLoggedUser()

        assertNotNull(result)
        assertEquals(user.id, result!!.id)
        assertEquals(user.email, result.email)
    }

    @Test
    fun `get not existing currently logged user`() {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        val exception = assertThrows<NotFoundException> {
            userService.currentlyLoggedUser()
        }

        assertTrue(exception.message!!.contains("jane.doe@soodoku.com"))
    }
}
