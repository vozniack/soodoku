package dev.vozniack.soodoku.core.service.extension

import dev.vozniack.soodoku.core.api.dto.FriendInvitationAcceptedPayload
import dev.vozniack.soodoku.core.api.dto.FriendInvitationReceivedPayload
import dev.vozniack.soodoku.core.api.dto.FriendInvitationRejectedPayload
import dev.vozniack.soodoku.core.api.dto.FriendInvitationRemovedPayload
import dev.vozniack.soodoku.core.api.dto.FriendRemovedPayload
import dev.vozniack.soodoku.core.api.dto.SseEventDto
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.types.SseEventType

fun buildFriendInvitationReceivedEvent(invitation: FriendInvitation): SseEventDto<Any> = SseEventDto(
    type = SseEventType.FRIEND_INVITATION_RECEIVED,
    payload = FriendInvitationReceivedPayload(invitation.id, invitation.sender.username)
)

fun buildFriendInvitationAcceptedEvent(invitation: FriendInvitation): SseEventDto<Any> = SseEventDto(
    type = SseEventType.FRIEND_INVITATION_ACCEPTED,
    payload = FriendInvitationAcceptedPayload(invitation.id, invitation.receiver.username)
)

fun buildFriendInvitationRejectedEvent(invitation: FriendInvitation): SseEventDto<Any> = SseEventDto(
    type = SseEventType.FRIEND_INVITATION_REJECTED,
    payload = FriendInvitationRejectedPayload(invitation.id, invitation.receiver.username)
)

fun buildFriendInvitationRemovedEvent(invitation: FriendInvitation): SseEventDto<Any> = SseEventDto(
    type = SseEventType.FRIEND_INVITATION_REMOVED,
    payload = FriendInvitationRemovedPayload(invitation.id, invitation.sender.username)
)

fun buildFriendRemovedEvent(friend: Friend): SseEventDto<Any> = SseEventDto(
    type = SseEventType.FRIEND_REMOVED,
    payload = FriendRemovedPayload(friend.user.username)
)
