package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.Game
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface GameRepository : CrudRepository<Game, UUID> {

    fun findFirstByUserIdAndFinishedAtIsNullOrderByUpdatedAtDesc(userId: UUID): Game?

    @Query(
        """
            SELECT g FROM Game g 
            WHERE g.user.id = :userId 
              AND (:finished = true AND g.finishedAt IS NOT NULL 
                   OR :finished = false AND g.finishedAt IS NULL)
            ORDER BY g.updatedAt DESC
        """
    )
    fun findByUserIdAndFinishedStatus(
        @Param("userId") userId: UUID, @Param("finished") finished: Boolean, pageable: Pageable
    ): Slice<Game>
}
