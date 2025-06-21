package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.GameSummary
import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface GameSummaryRepository : CrudRepository<GameSummary, UUID> {

    fun deleteByGameId(gameId: UUID)
}
