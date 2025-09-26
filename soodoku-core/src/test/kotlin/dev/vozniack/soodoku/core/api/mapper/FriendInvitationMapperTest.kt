package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.fixture.mockUser
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FriendInvitationMapperTest {

    @Test
    fun `map friend invitation to dto`() {
        val sender: User = mockUser("john.doe@soodku.com")
        val receiver: User = mockUser("jane.doe@soodoku.com")

        val invitation = FriendInvitation(
            id = UUID.randomUUID(),
            sender = sender,
            receiver = receiver,
            createdAt = LocalDateTime.now(),
            respondedAt = LocalDateTime.now()
        )

        val invitationDto: FriendInvitationDto = invitation.toDto()

        assertEquals(invitation.id, invitationDto.id)
        assertEquals(invitation.sender.username, invitationDto.sender.username)
        assertEquals(invitation.receiver.username, invitationDto.receiver.username)
        assertNotNull(invitationDto.createdAt)
        assertNotNull(invitationDto.respondedAt)
    }
}
