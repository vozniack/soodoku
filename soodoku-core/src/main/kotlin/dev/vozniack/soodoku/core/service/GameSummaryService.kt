package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.GameSummaryDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameSummary
import dev.vozniack.soodoku.core.domain.extension.toSoodoku
import dev.vozniack.soodoku.core.domain.repository.GameSummaryRepository
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
class GameSummaryService(private val gameSummaryRepository: GameSummaryRepository) {

    fun getSummary(difficulty: Difficulty?, victory: Boolean?, pageable: Pageable): Slice<GameSummaryDto> {
        val spec = Specification<GameSummary> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            difficulty?.let {
                predicates += cb.equal(root.get<Difficulty>("difficulty"), it)
            }

            victory?.let {
                predicates += cb.equal(root.get<Boolean>("victory"), it)
            }

            cb.and(*predicates.toTypedArray())
        }

        return gameSummaryRepository.findAll(spec, pageable).map { it.toDto() }
    }

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
                    victory = it.currentBoard.count { cell -> cell == '0' } == 0 && it.toSoodoku().status().conflicts.isEmpty(),
                    createdAt = game.createdAt,
                    finishedAt = game.finishedAt!!
                )
            )
        } ?: logger.debug { "Skipping summary for game ${game.id}" }
    }

    fun delete(gameId: UUID) {
        gameSummaryRepository.deleteByGameId(gameId)
    }

    companion object : KLogging()
}
