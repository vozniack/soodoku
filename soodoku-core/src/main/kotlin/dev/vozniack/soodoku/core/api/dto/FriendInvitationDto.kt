package dev.vozniack.soodoku.core.api.dto

import java.util.UUID

data class FriendInvitationDto(
    val id: UUID,

    val senderEmail: String,
    val receiverEmail: String,

    val createdAt: String,
    val respondedAt: String? = null
)

data class FriendInvitationRequestDto(
    val receiverEmail: String
)
