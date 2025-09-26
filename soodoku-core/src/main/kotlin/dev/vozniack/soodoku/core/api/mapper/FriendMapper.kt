package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.FriendDto
import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.util.toISOTime

fun Friend.toDto(): FriendDto = FriendDto(
    id = id,
    friend = friend.toSimpleDto(),
    since = since.toISOTime()
)
