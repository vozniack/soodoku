package dev.vozniack.soodoku.core.api.dto

import dev.vozniack.soodoku.core.domain.types.ConflictType
import dev.vozniack.soodoku.core.domain.types.Difficulty
import java.util.UUID

data class GameDto(
    val id: UUID,
    val userId: UUID? = null,

    val board: Array<IntArray>,
    val locks: List<List<Int>>,
    val conflicts: List<ConflictDto>,

    val difficulty: Difficulty,
    val missing: Int,
    val moves: Int,
    val realMoves: Int,

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

data class MoveDto(
    val row: Int,
    val col: Int,

    val value: Int
)
