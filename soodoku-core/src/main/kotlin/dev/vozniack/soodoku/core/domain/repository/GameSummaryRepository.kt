package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.GameSummary
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface GameSummaryRepository : JpaRepository<GameSummary, UUID>, JpaSpecificationExecutor<GameSummary> {

    fun deleteByGameId(gameId: UUID)
}
