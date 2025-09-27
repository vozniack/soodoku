package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.FriendDto
import dev.vozniack.soodoku.core.api.dto.UserSimpleDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.service.extension.buildFriendRemovedEvent
import jakarta.transaction.Transactional
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FriendService(
    private val friendRepository: FriendRepository,
    private val friendInvitationRepository: FriendInvitationRepository,
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun getFriends(): List<FriendDto> = friendRepository.findAllByUser(getCurrentlyLoggedUser()).map { it.toDto() }

    fun getFriendCandidates(search: String): List<UserSimpleDto> {
        val user: User = getCurrentlyLoggedUser()

        val friends = friendRepository.findAllByUser(user).map { it.friend }
        val invitedUsers = friendInvitationRepository.findAllPendingInvites(user)
            .flatMap { listOf(it.sender, it.receiver) }
            .distinctBy { it.id }

        val excluded = (friends + invitedUsers + user).map { it.id }.toSet()

        return userRepository.findByUsernameContainingIgnoreCase(search)
            .filter { it.id !in excluded }
            .map { UserSimpleDto(it.username) }
    }

    fun create(invitation: FriendInvitation) {
        if (invitation.status != InvitationStatus.PENDING) {
            throw ConflictException("Only pending invitations can be accepted")
        }

        if (friendRepository.friendshipExists(invitation.sender, invitation.receiver)) {
            throw ConflictException("Friendship already exists")
        }

        friendRepository.saveAll(
            listOf(
                Friend(user = invitation.sender, friend = invitation.receiver),
                Friend(user = invitation.receiver, friend = invitation.sender)
            )
        )
    }

    @Transactional
    fun remove(id: UUID) {
        val user: User = getCurrentlyLoggedUser()

        val friend = friendRepository.findById(id).orElseThrow {
            NotFoundException("Not found friend with id $id")
        }

        if (friend.user != user) {
            throw ConflictException("You don't have access to this resource")
        }

        friendRepository.deleteByUserAndFriend(friend.user, friend.friend)
        friendRepository.deleteByUserAndFriend(friend.friend, friend.user)

        notificationService.sendSseEvent(friend.friend.id, buildFriendRemovedEvent(friend))
    }

    private fun getCurrentlyLoggedUser(): User = userService.currentlyLoggedUser()
        ?: throw UnauthorizedException("You don't have access to this resource")
}
