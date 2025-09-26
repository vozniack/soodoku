package dev.vozniack.soodoku.core.api.dto

import java.util.UUID

data class FriendInvitationDto(
    val id: UUID,

    val sender: UserSimpleDto,
    val receiver: UserSimpleDto,

    val createdAt: String,
    val respondedAt: String? = null
)

data class FriendInvitationRequestDto(
    val receiverUsername: String
)
