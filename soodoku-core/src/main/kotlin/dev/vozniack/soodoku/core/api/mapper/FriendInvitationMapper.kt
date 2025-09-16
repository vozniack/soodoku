package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.util.toISOTime

fun FriendInvitation.toDto(): FriendInvitationDto = FriendInvitationDto(
    id = id,
    senderEmail = sender.email,
    receiverEmail = receiver.email,
    createdAt = createdAt.toISOTime(),
    respondedAt = respondedAt?.toISOTime()
)
