package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.util.toISOTime

fun FriendInvitation.toDto(): FriendInvitationDto = FriendInvitationDto(
    id = id,
    sender = sender.toSimpleDto(),
    receiver = receiver.toSimpleDto(),
    createdAt = createdAt.toISOTime(),
    respondedAt = respondedAt?.toISOTime()
)
