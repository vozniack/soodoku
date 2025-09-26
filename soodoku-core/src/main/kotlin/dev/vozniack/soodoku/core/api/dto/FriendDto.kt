package dev.vozniack.soodoku.core.api.dto

import java.util.UUID

data class FriendDto(
    val id: UUID,

    val friend: UserSimpleDto,

    val since: String
)
