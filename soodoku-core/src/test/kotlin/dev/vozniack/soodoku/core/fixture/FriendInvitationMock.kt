package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.FriendInvitationRequestDto

fun mockFriendInvitationRequestDto(receiverUsername: String = "janedoe"): FriendInvitationRequestDto =
    FriendInvitationRequestDto(
        receiverUsername = receiverUsername
    )
