package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class FriendServiceTest @Autowired constructor(
    private val friendService: FriendService,
    private val friendInvitationRepository: FriendInvitationRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up`() {
        friendInvitationRepository.deleteAll()
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `create friends`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = user, receiver = secondUser)
        )

        assertEquals(0, friendRepository.count())

        friendService.create(invitation)

        assertEquals(2, friendRepository.count())

        val friends: List<Friend> = friendRepository.findAll().toList()

        val firstFriendship: Friend = friends[0]
        assertEquals(user, firstFriendship.user)
        assertEquals(secondUser, firstFriendship.friend)

        val secondFriendship: Friend = friends[1]
        assertEquals(secondUser, secondFriendship.user)
        assertEquals(user, secondFriendship.friend)
    }

    @Test
    fun `create friends twice`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = user, receiver = secondUser)
        )

        assertEquals(0, friendRepository.count())

        friendService.create(invitation)

        assertThrows<ConflictException> {
            friendService.create(invitation)
        }
    }

    @Test
    fun `create friends while invitation already accepted`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = user, receiver = secondUser, status = InvitationStatus.ACCEPTED)
        )

        assertEquals(0, friendRepository.count())

        assertThrows<ConflictException> {
            friendService.create(invitation)
        }
    }
}
