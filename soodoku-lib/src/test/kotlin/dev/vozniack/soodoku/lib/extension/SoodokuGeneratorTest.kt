package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SoodokuGeneratorTest {

    @Test
    fun `generate easy board`() {
        val soodoku = Soodoku("0".repeat(81).mapBoard(), arrayOf(), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.EASY)
        assertEquals(Soodoku.Difficulty.EASY.emptyCells, soodoku.missingCells())
        assertEquals(soodoku.locks.size, 81 - soodoku.missingCells())
    }

    @Test
    fun `generate medium board`() {
        val soodoku = Soodoku("0".repeat(81).mapBoard(),arrayOf(), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.MEDIUM)
        assertEquals(Soodoku.Difficulty.MEDIUM.emptyCells, soodoku.missingCells())
        assertEquals(soodoku.locks.size, 81 - soodoku.missingCells())
    }

    @Test
    fun `generate hard board`() {
        val soodoku = Soodoku("0".repeat(81).mapBoard(),arrayOf(), listOf())
        assertTrue(soodoku.missingCells() == 81)

        soodoku.generate(Soodoku.Difficulty.HARD)
        assertEquals(Soodoku.Difficulty.HARD.emptyCells, soodoku.missingCells())
        assertEquals(soodoku.locks.size, 81 - soodoku.missingCells())
    }
}
