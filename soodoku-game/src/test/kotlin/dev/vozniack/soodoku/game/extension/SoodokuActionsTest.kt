package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku
import dev.vozniack.soodoku.game.defaultFlatBoard
import dev.vozniack.soodoku.game.defaultLock
import dev.vozniack.soodoku.game.exception.SoodokuMoveException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SoodokuActionsTest {

    @Test
    fun `get Soodoku status`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board.flatBoard(), status.board)
        assertEquals(soodoku.lock, status.lock)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(0, status.conflicts.size)
        assertFalse(status.done)
    }

    @Test
    fun `get Soodoku status after incorrect move`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())
        soodoku.move(4, 0, 8)

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board.flatBoard(), status.board)
        assertEquals(soodoku.lock, status.lock)
        assertEquals(soodoku.missingCells(), status.missingCells)
        assertEquals(2, status.conflicts.size)
        assertFalse(status.done)
    }

    @Test
    fun `get Soodoku status after solving`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())
        soodoku.solve()

        val status: Soodoku.Status = soodoku.status()

        assertEquals(soodoku.board.flatBoard(), status.board)
        assertEquals(soodoku.lock, status.lock)
        assertEquals(0, status.missingCells)
        assertEquals(0, status.conflicts.size)
        assertTrue(status.done)
    }

    @Test
    fun `make move`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())
        soodoku.move(4, 0, 8)

        assertNotEquals(defaultFlatBoard(), soodoku.board.flatBoard())
    }

    @Test
    fun `make incorrect move`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 9, 0)
        }
    }
}
