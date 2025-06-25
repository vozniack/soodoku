package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.GameMove
import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface GameMoveRepository : CrudRepository<GameMove, UUID>
