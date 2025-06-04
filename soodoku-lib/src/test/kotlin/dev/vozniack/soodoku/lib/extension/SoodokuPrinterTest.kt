package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import kotlin.test.Test

class SoodokuPrinterTest {

    @Test
    fun `print soodoku board`() {
        Soodoku(Soodoku.Difficulty.HARD).print()
    }
}
