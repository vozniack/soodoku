package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.FriendInvitationRequestDto

fun mockFriendInvitationRequestDto(receiverEmail: String = "jane.doe@soodoku.com"): FriendInvitationRequestDto =
    FriendInvitationRequestDto(
        receiverEmail = receiverEmail
    )
