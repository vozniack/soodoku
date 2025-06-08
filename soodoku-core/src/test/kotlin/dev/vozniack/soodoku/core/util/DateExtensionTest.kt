package dev.vozniack.soodoku.core.util

import dev.vozniack.soodoku.core.AbstractUnitTest
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateExtensionTest : AbstractUnitTest() {

    @Test
    fun `format LocalDateTime to ISO time`() {
        val dateTime = LocalDateTime.of(2025, 6, 8, 21, 37, 15)
        val isoString = dateTime.toISOTime()

        assertEquals("2025-06-08T21:37:15", isoString)
    }
}
