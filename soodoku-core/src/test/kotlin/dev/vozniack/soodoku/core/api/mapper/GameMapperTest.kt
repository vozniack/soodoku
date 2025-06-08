package dev.vozniack.soodoku.core.api.mapper

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.Move
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.types.ConflictType
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.util.toISOTime
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.flatLocks
import dev.vozniack.soodoku.lib.extension.move
import dev.vozniack.soodoku.lib.extension.status
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameMapperTest : AbstractUnitTest() {

    @Test
    fun `map game to dto for newly created game`() {
        val soodoku = Soodoku(Soodoku.Difficulty.EASY)
        val status = soodoku.status()

        val user = User(email = "john.doe@soodoku.com")

        val game = Game(
            initialBoard = status.board.flatBoard(),
            currentBoard = status.board.flatBoard(),
            locks = status.locks.flatLocks(),
            difficulty = Difficulty.EASY,
            user = user
        )

        val gameDto = game toDtoWithStatus status

        assertEquals(game.id, gameDto.id)
        assertEquals(user.id, gameDto.userId)
        assertEquals(9, gameDto.board.size)
        assertEquals(9, gameDto.board[0].size)

        assertEquals(status.locks.size, gameDto.locks.size)
        gameDto.locks.forEach { cell -> assertEquals(2, cell.size) }

        assertTrue(gameDto.conflicts.isEmpty())

        assertEquals(Soodoku.Difficulty.EASY.name, gameDto.difficulty.name)
        assertEquals(game.currentBoard.count { it == '0' }, gameDto.missing)
        assertEquals(game.moves.size, gameDto.moves)

        assertEquals(game.createdAt.toISOTime(), gameDto.createdAt)
        assertNull(game.updatedAt?.toISOTime())
        assertNull(game.finishedAt?.toISOTime())
    }

    @Test
    fun `map game to dto for board with row conflict`() {
        val emptyBoard = "0".repeat(81)
        val emptyLocks = ""

        val soodoku = Soodoku(emptyBoard, emptyLocks)

        val user = User(email = "john.doe@soodoku.com")

        val game = Game(
            initialBoard = emptyBoard,
            currentBoard = emptyBoard,
            locks = emptyLocks,
            difficulty = Difficulty.EASY,
            user = user
        )

        val row = 0
        val col1 = 0
        val col2 = 1
        val value = 5

        soodoku.move(row, col1, value)
        var status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.add(Move(game = it, row = row, col = col1, before = 0, after = value))
        }

        soodoku.move(row, col2, value)
        status = soodoku.status()

        game.apply {
            currentBoard = status.board.flatBoard()
            updatedAt = LocalDateTime.now()
        }.also {
            it.moves.add(Move(game = it, row = row, col = col2, before = 0, after = value))
        }

        val gameDto = game toDtoWithStatus status

        assertEquals(game.id, gameDto.id)
        assertEquals(user.id, gameDto.userId)
        assertTrue(gameDto.conflicts.isNotEmpty())

        val conflict = gameDto.conflicts.first()

        assertEquals(ConflictType.ROW, conflict.type)
        assertEquals(value, conflict.value)
        assertEquals(row, conflict.index)

        assertTrue(
            conflict.cells.containsAll(
                listOf(
                    listOf(row, col1),
                    listOf(row, col2)
                )
            )
        )

        assertEquals(Soodoku.Difficulty.EASY.name, gameDto.difficulty.name)
        assertEquals(79, gameDto.missing)
        assertEquals(2, gameDto.moves)
    }
}
