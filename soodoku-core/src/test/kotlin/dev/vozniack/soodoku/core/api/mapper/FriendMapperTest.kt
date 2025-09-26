package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.FriendDto
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.fixture.mockUser
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FriendMapperTest {

    @Test
    fun `map friend to dto`() {
        val user: User = mockUser("john.doe@soodku.com")
        val secondUser: User = mockUser("jane.doe@soodoku.com")

        val friend = Friend(UUID.randomUUID(), user, secondUser)
        val friendDto: FriendDto = friend.toDto()

        assertEquals(friend.id, friendDto.id)
        assertEquals(secondUser.username, friendDto.friend.username)
        assertNotNull(friendDto.since)
    }
}
