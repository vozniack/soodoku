package dev.vozniack.soodoku.core.api.dto

import dev.vozniack.soodoku.core.domain.types.Difficulty
import java.util.UUID

data class GameHistoryDto(
    val userId: UUID,
    val gameId: UUID,

    val difficulty: Difficulty,

    val duration: Long,

    val missingCells: Int,
    val totalMoves: Int,
    val usedHints: Int,

    val victory: Boolean,

    val createdAt: String,
    val finishedAt: String
)
