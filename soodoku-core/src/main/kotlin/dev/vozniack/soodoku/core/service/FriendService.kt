package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import org.springframework.stereotype.Service

@Service
class FriendService(private val friendRepository: FriendRepository) {

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
}
