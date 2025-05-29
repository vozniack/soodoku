package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SoodokuGeneratorTest {

    @Test
    fun `generate easy board`() {
        val soodoku = Soodoku("0".repeat(81), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.EASY)
        assertEquals(soodoku.missingCells(), Soodoku.Difficulty.EASY.emptyCells)
        assertEquals(soodoku.lock.size, 81 - soodoku.missingCells())
    }

    @Test
    fun `generate medium board`() {
        val soodoku = Soodoku("0".repeat(81), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.MEDIUM)
        assertEquals(soodoku.missingCells(), Soodoku.Difficulty.MEDIUM.emptyCells)
        assertEquals(soodoku.lock.size, 81 - soodoku.missingCells())
    }

    @Test
    fun `generate hard board`() {
        val soodoku = Soodoku("0".repeat(81), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.HARD)
        assertEquals(soodoku.missingCells(), Soodoku.Difficulty.HARD.emptyCells)
        assertEquals(soodoku.lock.size, 81 - soodoku.missingCells())
    }
}
