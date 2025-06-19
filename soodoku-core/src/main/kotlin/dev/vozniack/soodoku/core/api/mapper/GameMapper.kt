package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.api.dto.ConflictDto
import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.MoveDto
import dev.vozniack.soodoku.core.api.dto.NoteDto
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.extension.parseNotes
import dev.vozniack.soodoku.core.domain.types.ConflictType
import dev.vozniack.soodoku.core.util.toISOTime
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.mapBoard
import dev.vozniack.soodoku.lib.extension.mapLocks

infix fun Game.toDtoWithStatus(status: Soodoku.Status): GameDto = GameDto(
    id = id,
    userId = user?.id,

    board = currentBoard.mapBoard(),
    solved = finishedAt?.let { this.solvedBoard.mapBoard() },
    locks = locks.mapLocks().map { listOf(it.first, it.second) },
    conflicts = status.conflicts.map { it.toDto() },
    notes = parseNotes().map { (pos, values) ->
        NoteDto(row = pos.first, col = pos.second, values = values.toTypedArray())
    },

    difficulty = difficulty,

    missing = currentBoard.count { it == '0' },
    hints = hints,

    moves = moves.map { MoveDto(it.row, it.col, it.before, it.after, it.type, it.revertedAt != null) },

    createdAt = createdAt.toISOTime(),
    updatedAt = updatedAt?.toISOTime(),
    finishedAt = finishedAt?.toISOTime()
)

private fun Soodoku.Conflict.toDto(): ConflictDto = ConflictDto(
    type = ConflictType.valueOf(type.name),
    value = value,
    index = index,
    cells = cells.map { listOf(it.first, it.second) }
)
