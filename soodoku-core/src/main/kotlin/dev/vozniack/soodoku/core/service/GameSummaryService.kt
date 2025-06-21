package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameSummary
import dev.vozniack.soodoku.core.domain.extension.toSoodoku
import dev.vozniack.soodoku.core.domain.repository.GameSummaryRepository
import dev.vozniack.soodoku.core.domain.types.MoveType
import dev.vozniack.soodoku.core.internal.logging.KLogging
import dev.vozniack.soodoku.lib.extension.status
import java.time.Duration.between
import java.util.UUID
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class GameSummaryService(private val gameSummaryRepository: GameSummaryRepository) {

    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 2048))
    fun summarize(game: Game) {
        game.takeIf { it.user != null && game.finishedAt != null }?.let {
            gameSummaryRepository.save(
                GameSummary(
                    game = it,
                    user = it.user!!,
                    difficulty = it.difficulty,
                    duration = between(it.createdAt, it.finishedAt).seconds,
                    missingCells = it.currentBoard.count { cell -> cell == '0' },
                    totalMoves = it.moves.size,
                    usedHints = it.moves.count { move -> move.type == MoveType.HINT },
                    victory = it.currentBoard.count { cell -> cell == '0' } == 0 &&
                            it.toSoodoku().status().conflicts.isEmpty()
                )
            )
        } ?: logger.debug { "Skipping summary for game ${game.id}" }
    }

    fun delete(gameId: UUID) {
        gameSummaryRepository.deleteByGameId(gameId)
    }

    companion object : KLogging()
}
