package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.Move
import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface MoveRepository : CrudRepository<Move, UUID>
