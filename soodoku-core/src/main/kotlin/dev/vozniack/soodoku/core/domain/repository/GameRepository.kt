package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.Game
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface GameRepository : CrudRepository<Game, UUID> {

    @Query(
        value = """
            SELECT * FROM games
            WHERE user_id = :userId AND finished_at IS NULL
            ORDER BY COALESCE(updated_at, started_at) DESC
        """, nativeQuery = true
    )
    fun findOngoingGames(@Param("userId") userId: UUID, pageable: Pageable): Slice<Game>
}
