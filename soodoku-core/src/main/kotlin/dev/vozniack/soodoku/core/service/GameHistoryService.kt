package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.GameHistoryDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameHistory
import dev.vozniack.soodoku.core.domain.extension.toSoodoku
import dev.vozniack.soodoku.core.domain.repository.GameHistoryRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.MoveType
import dev.vozniack.soodoku.core.internal.logging.KLogging
import dev.vozniack.soodoku.lib.extension.status
import jakarta.persistence.criteria.Predicate
import java.time.Duration.between
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.domain.Specification
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class GameHistoryService(private val gameHistoryRepository: GameHistoryRepository) {

    fun get(difficulty: Difficulty?, victory: Boolean?, pageable: Pageable): Slice<GameHistoryDto> {
        val spec = Specification<GameHistory> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            difficulty?.let {
                predicates += cb.equal(root.get<Difficulty>("difficulty"), it)
            }

            victory?.let {
                predicates += cb.equal(root.get<Boolean>("victory"), it)
            }

            cb.and(*predicates.toTypedArray())
        }

        return gameHistoryRepository.findAll(spec, pageable).map { it.toDto() }
    }

    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 2048))
    fun save(game: Game) {
        game.takeIf { it.user != null && game.finishedAt != null }?.let {
            gameHistoryRepository.save(
                GameHistory(
                    game = it,
                    user = it.user!!,
                    difficulty = it.difficulty,
                    duration = it.sessions.filter { session -> session.pausedAt != null }
                        .sumOf { session -> between(session.startedAt, session.pausedAt).seconds },
                    missingCells = it.currentBoard.count { cell -> cell == '0' },
                    totalMoves = it.moves.size,
                    usedHints = it.moves.count { move -> move.type == MoveType.HINT },
                    victory = it.currentBoard.count { cell -> cell == '0' } == 0 && it.toSoodoku()
                        .status().conflicts.isEmpty(),
                    startedAt = game.startedAt,
                    finishedAt = game.finishedAt!!
                )
            )
        } ?: logger.debug { "Skipping saving history for game ${game.id}" }
    }

    fun delete(gameId: UUID) {
        gameHistoryRepository.deleteByGameId(gameId)
    }

    companion object : KLogging()
}
