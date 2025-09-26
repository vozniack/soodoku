package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.fixture.mockFriendInvitationRequestDto
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class FriendInvitationServiceTest @Autowired constructor(
    private val friendInvitationService: FriendInvitationService,
    private val friendInvitationRepository: FriendInvitationRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up`() {
        friendInvitationRepository.deleteAll()
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `get sent invites`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val thirdUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = secondUser))
        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = thirdUser))
        friendInvitationRepository.save(FriendInvitation(sender = secondUser, receiver = thirdUser))

        authenticate(user.email)
        assertEquals(2, friendInvitationService.getSent().count())

        authenticate(secondUser.email)
        assertEquals(1, friendInvitationService.getSent().count())

        authenticate(thirdUser.email)
        assertEquals(0, friendInvitationService.getSent().count())
    }

    @Test
    fun `get sent invites with anonymous user`() {
        assertThrows<UnauthorizedException> { friendInvitationService.getSent() }
    }

    @Test
    fun `get received invites`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val thirdUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = secondUser))
        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = thirdUser))
        friendInvitationRepository.save(FriendInvitation(sender = secondUser, receiver = thirdUser))

        authenticate(user.email)
        assertEquals(0, friendInvitationService.getReceived().count())

        authenticate(secondUser.email)
        assertEquals(1, friendInvitationService.getReceived().count())

        authenticate(thirdUser.email)
        assertEquals(2, friendInvitationService.getReceived().count())
    }

    @Test
    fun `get received invites with anonymous user`() {
        assertThrows<UnauthorizedException> { friendInvitationService.getReceived() }
    }

    @Test
    fun `invite user`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser(email = "jane.doe@soodoku.com", username = "janedoe"))

        authenticate(sender.email)

        friendInvitationService.invite(mockFriendInvitationRequestDto(receiverUsername = receiver.username))

        val invitations = friendInvitationRepository.findAll()
        assertEquals(1, invitations.count())
    }

    @Test
    fun `invite not existing user`() {
        val sender = userRepository.save(mockUser())
        authenticate(sender.email)

        assertThrows<NotFoundException> {
            friendInvitationService.invite(mockFriendInvitationRequestDto(receiverUsername = "someguy"))
        }
    }

    @Test
    fun `invite user which is already invited`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser(email = "jane.doe@soodoku.com", username = "janedoe"))

        authenticate(sender.email)

        friendInvitationService.invite(mockFriendInvitationRequestDto(receiverUsername = receiver.username))

        assertThrows<ConflictException> {
            friendInvitationService.invite(mockFriendInvitationRequestDto(receiverUsername = receiver.username))
        }

        val invitations = friendInvitationRepository.findAll()
        assertEquals(1, invitations.count())
    }

    @Test
    fun `invite with anonymous user`() {
        assertThrows<UnauthorizedException> { friendInvitationService.invite(mockFriendInvitationRequestDto()) }
    }

    @Test
    fun `accept invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(invitation.id).get().status)

        assertEquals(0, friendRepository.count())

        authenticate(receiver.email)
        friendInvitationService.accept(invitation.id)

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.ACCEPTED, friendInvitationRepository.findById(invitation.id).get().status)

        assertEquals(2, friendRepository.count())
    }

    @Test
    fun `accept invitation while both invited themselves`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val firstInvitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        val secondInvitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = receiver, receiver = sender)
        )

        assertEquals(2, friendInvitationRepository.count())
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(firstInvitation.id).get().status)
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(secondInvitation.id).get().status)

        assertEquals(0, friendRepository.count())

        authenticate(receiver.email)
        friendInvitationService.accept(firstInvitation.id)

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.ACCEPTED, friendInvitationRepository.findById(firstInvitation.id).get().status)

        assertEquals(2, friendRepository.count())
    }

    @Test
    fun `accept invitation with other user`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        authenticate(sender.email)

        assertThrows<ConflictException> {
            friendInvitationService.accept(invitation.id)
        }
    }

    @Test
    fun `accept not existing invitation`() {
        val sender = userRepository.save(mockUser())
        authenticate(sender.email)

        assertThrows<NotFoundException> {
            friendInvitationService.accept(UUID.randomUUID())
        }
    }

    @Test
    fun `accept invitation with anonymous user`() {
        assertThrows<UnauthorizedException> { friendInvitationService.accept(UUID.randomUUID()) }
    }

    @Test
    fun `reject invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(invitation.id).get().status)

        assertEquals(0, friendRepository.count())

        authenticate(receiver.email)
        friendInvitationService.reject(invitation.id)

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.REJECTED, friendInvitationRepository.findById(invitation.id).get().status)

        assertEquals(0, friendRepository.count())
    }

    @Test
    fun `reject invitation while both invited themselves`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val firstInvitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        val secondInvitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = receiver, receiver = sender)
        )

        assertEquals(2, friendInvitationRepository.count())
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(firstInvitation.id).get().status)
        assertEquals(InvitationStatus.PENDING, friendInvitationRepository.findById(secondInvitation.id).get().status)

        assertEquals(0, friendRepository.count())

        authenticate(receiver.email)
        friendInvitationService.reject(firstInvitation.id)

        assertEquals(1, friendInvitationRepository.count())
        assertEquals(InvitationStatus.REJECTED, friendInvitationRepository.findById(firstInvitation.id).get().status)

        assertEquals(0, friendRepository.count())
    }

    @Test
    fun `reject not existing invitation`() {
        val sender = userRepository.save(mockUser())
        authenticate(sender.email)

        assertThrows<NotFoundException> {
            friendInvitationService.reject(UUID.randomUUID())
        }
    }

    @Test
    fun `reject invitation with anonymous user`() {
        assertThrows<UnauthorizedException> { friendInvitationService.reject(UUID.randomUUID()) }
    }

    @Test
    fun `delete invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        assertEquals(1, friendInvitationRepository.count())

        authenticate(sender.email)
        friendInvitationService.delete(invitation.id)

        assertEquals(0, friendInvitationRepository.count())
    }

    @Test
    fun `delete accepted invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver, status = InvitationStatus.ACCEPTED)
        )

        assertEquals(1, friendInvitationRepository.count())

        authenticate(sender.email)

        assertThrows<ConflictException> {
            friendInvitationService.delete(invitation.id)
        }
    }

    @Test
    fun `delete rejected invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver, status = InvitationStatus.ACCEPTED)
        )

        assertEquals(1, friendInvitationRepository.count())

        authenticate(sender.email)

        assertThrows<ConflictException> {
            friendInvitationService.delete(invitation.id)
        }
    }

    @Test
    fun `delete not existing invitation`() {
        val sender = userRepository.save(mockUser())

        authenticate(sender.email)

        assertThrows<NotFoundException> {
            friendInvitationService.delete(UUID.randomUUID())
        }
    }
}
