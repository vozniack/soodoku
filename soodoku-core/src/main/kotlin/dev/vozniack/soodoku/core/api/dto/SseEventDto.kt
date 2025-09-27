package dev.vozniack.soodoku.core.api.dto

import dev.vozniack.soodoku.core.domain.types.SseEventType
import java.util.UUID

data class SseEventDto<T>(
    val type: SseEventType,
    val payload: T
)

data class FriendInvitationReceivedPayload(
    val invitationId: UUID,
    val senderUsername: String
)

data class FriendInvitationAcceptedPayload(
    val invitationId: UUID,
    val receiverUsername: String
)

data class FriendInvitationRejectedPayload(
    val invitationId: UUID,
    val receiverUsername: String
)

data class FriendInvitationRemovedPayload(
    val invitationId: UUID,
    val senderUsername: String
)

data class FriendRemovedPayload(
    val friendUsername: String
)
