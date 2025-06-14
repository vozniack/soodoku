package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.defaultBoard
import dev.vozniack.soodoku.lib.defaultLocks
import dev.vozniack.soodoku.lib.defaultSolved
import dev.vozniack.soodoku.lib.exception.SoodokuMappingException
import dev.vozniack.soodoku.lib.exception.SoodokuMoveException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.assertThrows

class SoodokuActionsTest {

    @Test
    fun `get Soodoku status`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(0, status.conflicts.size)
    }

    @Test
    fun `get Soodoku status after incorrect move`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())
        soodoku.move(4, 0, 8)

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(2, status.conflicts.size)
    }

    @Test
    fun `get Soodoku status after solving`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())
        soodoku.solve()

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(0, status.missingCells)
        assertEquals(0, status.conflicts.size)
    }

    @Test
    fun `get Soodoku value for specific cell`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        assertEquals(soodoku.board[1][1], soodoku.value(1, 1))
    }

    @Test
    fun `get Soodoku value for incorrect row`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        assertThrows<SoodokuMappingException> {
            soodoku.value(9, 1)
        }

        assertThrows<SoodokuMappingException> {
            soodoku.value(-1, 1)
        }
    }

    @Test
    fun `get Soodoku value for incorrect column`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        assertThrows<SoodokuMappingException> {
            soodoku.value(1, 9)
        }

        assertThrows<SoodokuMappingException> {
            soodoku.value(1, -1)
        }
    }

    @Test
    fun `make move`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())
        val initialFlatBoard: String = soodoku.board.flatBoard()

        soodoku.move(4, 0, 8)

        assertNotEquals(initialFlatBoard, soodoku.board.flatBoard())
    }

    @Test
    fun `make incorrect move`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 9, 0)
        }
    }
}
