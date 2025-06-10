package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.mock.mockUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserMapperTest : AbstractUnitTest() {

    @Test
    fun `map user to dto`() {
        val user: User = mockUser()
        val userDto: UserDto = user.toDto()

        assertEquals(user.id, userDto.id)
        assertEquals(user.email, userDto.email)
        assertEquals(user.username, userDto.username)
    }
}
