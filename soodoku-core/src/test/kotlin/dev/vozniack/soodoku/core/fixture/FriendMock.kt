package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.User
import java.util.UUID

fun mockFriend(
    id: UUID = UUID.randomUUID(),
    user: User = mockUser(),
    friend: User = mockUser(email = "jane.doe@soodku.com", username = "janedoe")
): Friend = Friend(
    id = id,
    user = user,
    friend = friend
)
