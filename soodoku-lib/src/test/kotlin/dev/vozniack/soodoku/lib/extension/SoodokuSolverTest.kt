package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import kotlin.test.Test
import kotlin.test.assertTrue

class SoodokuSolverTest {

    @Test
    fun `solve easy soodoku`() {
        val soodoku = Soodoku(Soodoku.Difficulty.EASY)
        assertTrue(soodoku.missingCells() != 0)

        soodoku.solve()

        assertTrue(soodoku.missingCells() == 0)
    }

    @Test
    fun `solve medium soodoku`() {
        val soodoku = Soodoku(Soodoku.Difficulty.MEDIUM)
        assertTrue(soodoku.missingCells() != 0)

        soodoku.solve()

        assertTrue(soodoku.missingCells() == 0)
    }

    @Test
    fun `solve hard soodoku`() {
        val soodoku = Soodoku(Soodoku.Difficulty.HARD)
        assertTrue(soodoku.missingCells() != 0)

        soodoku.solve()

        assertTrue(soodoku.missingCells() == 0)
    }
}
