package dev.vozniack.soodoku.cli

import kotlin.test.Test
import kotlin.test.assertNotNull

class SoodokuCliTest {

    @Test
    fun `context test`() {
        assertNotNull(SoodokuCli())
    }
}
