package dev.vozniack.soodoku.core.api.dto

import dev.vozniack.soodoku.core.domain.types.ConflictType
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.MoveType
import java.util.UUID

data class GameDto(
    val id: UUID,
    val userId: UUID? = null,

    val board: Array<IntArray>,
    val locks: List<List<Int>>,
    val conflicts: List<ConflictDto>,

    val difficulty: Difficulty,

    val missing: Int,
    val hints: Int,

    val moves: List<MoveDto>,

    val createdAt: String,
    val updatedAt: String?,
    val finishedAt: String?
)

data class ConflictDto(
    val type: ConflictType,
    val value: Int,
    val index: Int,

    val cells: List<List<Int>>
)

data class NewGameDto(
    val difficulty: Difficulty,
)

data class NewMoveDto(
    val row: Int,
    val col: Int,

    val value: Int,
)

data class MoveDto(
    val row: Int,
    val col: Int,

    val before: Int,
    val after: Int,

    val type: MoveType,
    val reverted: Boolean
)
