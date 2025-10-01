package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.FriendInvitationRequestDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import java.util.UUID

fun mockFriendInvitationRequestDto(receiverUsername: String = "janedoe"): FriendInvitationRequestDto =
    FriendInvitationRequestDto(
        receiverUsername = receiverUsername
    )

fun mockFriendInvitation(
    id: UUID = UUID.randomUUID(),
    sender: User = mockUser(),
    receiver: User = mockUser(email = "jane.doe@soodoku.com", username = "janedoe")
): FriendInvitation = FriendInvitation(
    id = id,
    sender = sender,
    receiver = receiver
)
