package dev.vozniack.soodoku.core.api.dto.sse

import java.util.UUID

sealed interface SseEventPayload

data class FriendInvitationReceivedEventPayload(
    val invitationId: UUID,
    val senderUsername: String
) : SseEventPayload

data class FriendInvitationAcceptedEventPayload(
    val invitationId: UUID,
    val receiverUsername: String
) : SseEventPayload

data class FriendInvitationRejectedEventPayload(
    val invitationId: UUID,
    val receiverUsername: String
) : SseEventPayload

data class FriendInvitationRemovedEventPayload(
    val invitationId: UUID,
    val senderUsername: String
) : SseEventPayload

data class FriendRemovedEventPayload(
    val friendUsername: String
) : SseEventPayload
