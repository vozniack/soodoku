package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.GameHistory
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface GameHistoryRepository : JpaRepository<GameHistory, UUID>, JpaSpecificationExecutor<GameHistory> {

    fun deleteByGameId(gameId: UUID)
}
