package dev.vozniack.soodoku.core.api.dto.sse

import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation

data class SseEventDto<T : SseEventPayload>(
    val event: SseEvent,
    val payload: T
) {
    companion object {
        fun friendInvitationReceived(inv: FriendInvitation) = SseEventDto(
            event = SseEvent.FRIEND_INVITATION_RECEIVED,
            payload = FriendInvitationReceivedEventPayload(inv.id, inv.sender.username)
        )

        fun friendInvitationAccepted(inv: FriendInvitation) = SseEventDto(
            event = SseEvent.FRIEND_INVITATION_ACCEPTED,
            payload = FriendInvitationAcceptedEventPayload(inv.id, inv.receiver.username)
        )

        fun friendInvitationRejected(inv: FriendInvitation) = SseEventDto(
            event = SseEvent.FRIEND_INVITATION_REJECTED,
            payload = FriendInvitationRejectedEventPayload(inv.id, inv.receiver.username)
        )

        fun friendInvitationRemoved(inv: FriendInvitation) = SseEventDto(
            event = SseEvent.FRIEND_INVITATION_REMOVED,
            payload = FriendInvitationRemovedEventPayload(inv.id, inv.sender.username)
        )

        fun friendRemoved(friend: Friend) = SseEventDto(
            event = SseEvent.FRIEND_REMOVED,
            payload = FriendRemovedEventPayload(friend.user.username)
        )
    }
}

enum class SseEvent {
    FRIEND_INVITATION_RECEIVED,
    FRIEND_INVITATION_ACCEPTED,
    FRIEND_INVITATION_REJECTED,
    FRIEND_INVITATION_REMOVED,
    FRIEND_REMOVED
}
