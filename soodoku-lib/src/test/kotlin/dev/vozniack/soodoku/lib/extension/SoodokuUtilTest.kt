package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import kotlin.test.Test
import kotlin.test.assertEquals

class SoodokuUtilTest {

    @Test
    fun `count missing cells`() {
        val soodoku = Soodoku(Soodoku.Difficulty.EASY)

        assertEquals(Soodoku.Difficulty.EASY.emptyCells, soodoku.missingCells())
    }
}
