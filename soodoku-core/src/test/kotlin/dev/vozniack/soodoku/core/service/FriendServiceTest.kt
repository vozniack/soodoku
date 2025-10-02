package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.fixture.mockFriend
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
    fun `get friends`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        friendRepository.save(mockFriend(user = user, friend = secondUser))
        friendRepository.save(mockFriend(user = secondUser, friend = user))

        authenticate(user.email)

        val userFriends = friendService.getFriends()

        assertEquals(1, userFriends.size)
        assertEquals(secondUser.username, userFriends.first().friend.username)

        authenticate(secondUser.email)

        val secondUserFriends = friendService.getFriends()

        assertEquals(1, secondUserFriends.size)
        assertEquals(user.username, secondUserFriends.first().friend.username)
    }

    @Test
    fun `get friends with anonymous user`() {
        assertThrows<UnauthorizedException> {
            friendService.getFriends()
        }
    }

    @Test
    fun `get friend candidates`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val thirdUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))
        val fourthUser = userRepository.save(mockUser("jan.doe@soodoku.com", username = "jandoe"))

        friendRepository.save(mockFriend(user = user, friend = secondUser))
        friendRepository.save(mockFriend(user = secondUser, friend = user))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = thirdUser))

        authenticate(user.email)

        val friendCandidates = friendService.getFriendCandidates("jan")
        assertEquals(1, friendCandidates.size)
        assertEquals(fourthUser.username, friendCandidates.first().username)

        val noCandidates = friendService.getFriendCandidates("jenny")
        assertEquals(0, noCandidates.size)
    }

    @Test
    fun `get friend candidates with anonymous user`() {
        assertThrows<UnauthorizedException> {
            friendService.getFriendCandidates("doe")
        }
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

    @Test
    fun `remove friend`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val friend = friendRepository.save(mockFriend(user = user, friend = secondUser))
        friendRepository.save(mockFriend(user = secondUser, friend = user))

        assertEquals(2, friendRepository.count())

        authenticate(user.email)

        friendService.remove(friend.id)

        assertEquals(0, friendRepository.count())
    }

    @Test
    fun `remove not belonging friend`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        friendRepository.save(mockFriend(user = user, friend = secondUser))
        val friend = friendRepository.save(mockFriend(user = secondUser, friend = user))

        assertEquals(2, friendRepository.count())

        authenticate(user.email)

        assertThrows<ConflictException> {
            friendService.remove(friend.id)
        }
    }

    @Test
    fun `remove not existing friend`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        assertThrows<NotFoundException> {
            friendService.remove(UUID.randomUUID())
        }
    }

    @Test
    fun `remove friend with anonymous user`() {
        assertThrows<UnauthorizedException> {
            friendService.remove(UUID.randomUUID())
        }
    }
}
