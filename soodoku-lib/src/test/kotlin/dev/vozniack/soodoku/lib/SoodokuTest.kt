package dev.vozniack.soodoku.lib

import dev.vozniack.soodoku.lib.extension.missingCells
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SoodokuTest {

    @Test
    fun `initialize new Soodoku board`() {
        val soodoku = Soodoku(Soodoku.Difficulty.EASY)

        assertNotNull(soodoku.board)
        assertNotNull(soodoku.lock)

        assertEquals(Soodoku.Difficulty.EASY.emptyCells, soodoku.missingCells())
    }

    @Test
    fun `initialize existing Soodoku flat board`() {
        val soodoku = Soodoku(defaultFlatBoard(), defaultLock())

        assertNotNull(soodoku.board)
        assertEquals(24, soodoku.missingCells())
    }

    @Test
    fun `initialize existing Soodoku board`() {
        val soodoku = Soodoku(defaultBoard(), defaultLock())

        assertNotNull(soodoku.board)
        assertEquals(24, soodoku.missingCells())
    }
}
