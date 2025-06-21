package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.Game
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, UUID> {

    fun findFirstByUserIdAndFinishedAtIsNullOrderByUpdatedAtDesc(userId: UUID): Game?

    fun findByUserIdAndFinishedAtIsNullOrderByUpdatedAtDesc(userId: UUID, pageable: Pageable): Slice<Game>
}
