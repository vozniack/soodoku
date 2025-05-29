package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku
import kotlin.test.Test

class SoodokuPrinterTest {

    @Test
    fun `print soodoku board`() {
        Soodoku(Soodoku.Difficulty.HARD).print()
    }
}
