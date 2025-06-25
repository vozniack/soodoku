package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameHistory
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.GameType
import java.time.LocalDateTime

fun mockGameHistory(
    user: User,
    game: Game,
    type: GameType = GameType.RANDOM,
    difficulty: Difficulty = Difficulty.EASY,
    duration: Long = 2137,
    missingCells: Int = 0,
    totalMoves: Int = 40,
    usedHints: Int = 2,
    victory: Boolean = true,
    startedAt: LocalDateTime = LocalDateTime.now().minusHours(2),
    finishedAt: LocalDateTime = LocalDateTime.now()
): GameHistory = GameHistory(
    user = user,
    game = game,
    type = type,
    difficulty = difficulty,
    duration = duration,
    missingCells = missingCells,
    totalMoves = totalMoves,
    usedHints = usedHints,
    victory = victory,
    startedAt = startedAt,
    finishedAt = finishedAt
)
