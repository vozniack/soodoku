package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.defaultBoard
import dev.vozniack.soodoku.lib.defaultLocks
import dev.vozniack.soodoku.lib.exception.SoodokuMoveException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SoodokuActionsTest {

    @Test
    fun `get Soodoku status`() {
        val soodoku = Soodoku(defaultBoard(), defaultLocks())

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(0, status.conflicts.size)
        assertFalse(status.done)
    }

    @Test
    fun `get Soodoku status after incorrect move`() {
        val soodoku = Soodoku(defaultBoard(), defaultLocks())
        soodoku.move(4, 0, 8)

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(2, status.conflicts.size)
        assertFalse(status.done)
    }

    @Test
    fun `get Soodoku status after solving`() {
        val soodoku = Soodoku(defaultBoard(), defaultLocks())
        soodoku.solve()

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board, status.board)
        assertEquals(soodoku.locks, status.locks)
        assertEquals(0, status.missingCells)
        assertEquals(0, status.conflicts.size)
        assertTrue(status.done)
    }

    @Test
    fun `make move`() {
        val soodoku = Soodoku(defaultBoard(), defaultLocks())
        val initialFlatBoard: String = soodoku.board.flatBoard()

        soodoku.move(4, 0, 8)

        assertNotEquals(initialFlatBoard, soodoku.board.flatBoard())
    }

    @Test
    fun `make incorrect move`() {
        val soodoku = Soodoku(defaultBoard(), defaultLocks())

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 9, 0)
        }
    }
}
