package dev.vozniack.soodoku.core.util

import dev.vozniack.soodoku.core.AbstractUnitTest
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class SyntaxExtensionTest : AbstractUnitTest() {

    @Test
    fun `throwIfTrue throws exception when block returns true`() {
        val exception = assertThrows<IllegalArgumentException> {
            throwIfTrue(IllegalArgumentException("Something is no yes")) { true }
        }

        assertEquals("Something is no yes", exception.message)
    }

    @Test
    fun `throwIfTrue does not throw when block returns false`() {
        assertDoesNotThrow {
            throwIfTrue(IllegalArgumentException("Should not throw")) { false }
        }
    }
}
