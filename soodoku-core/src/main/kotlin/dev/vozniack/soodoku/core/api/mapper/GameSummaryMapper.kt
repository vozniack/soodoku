package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.GameSummaryDto
import dev.vozniack.soodoku.core.domain.entity.GameSummary
import dev.vozniack.soodoku.core.util.toISOTime

fun GameSummary.toDto(): GameSummaryDto = GameSummaryDto(
    userId = user.id,
    gameId = game.id,
    difficulty = difficulty,
    duration = duration,
    missingCells = missingCells,
    totalMoves = totalMoves,
    usedHints = usedHints,
    victory = victory,
    createdAt = createdAt.toISOTime(),
    finishedAt = finishedAt.toISOTime()
)
