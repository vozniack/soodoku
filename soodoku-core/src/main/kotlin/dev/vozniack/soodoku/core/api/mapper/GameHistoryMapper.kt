package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.GameHistoryDto
import dev.vozniack.soodoku.core.domain.entity.GameHistory
import dev.vozniack.soodoku.core.util.toISOTime

fun GameHistory.toDto(): GameHistoryDto = GameHistoryDto(
    userId = user.id,
    gameId = game.id,
    difficulty = difficulty,
    duration = duration,
    missingCells = missingCells,
    totalMoves = totalMoves,
    usedHints = usedHints,
    victory = victory,
    startedAt = startedAt.toISOTime(),
    finishedAt = finishedAt.toISOTime()
)
