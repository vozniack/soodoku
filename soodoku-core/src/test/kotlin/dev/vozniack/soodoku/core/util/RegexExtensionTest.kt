package dev.vozniack.soodoku.core.util

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RegexExtensionTest {

    @Test
    fun `match email regex`() {
        assertTrue("john.doe@soodoku.com".matchesEmailRegex())
        assertFalse("john.doe@soodoku".matchesEmailRegex())
        assertFalse("soodoku.com".matchesEmailRegex())
    }

    @Test
    fun `match password regex`() {
        assertTrue("pasSword1!".matchesPasswordRegex())
        assertFalse("password1!".matchesPasswordRegex())
        assertFalse("pAssword!".matchesPasswordRegex())
        assertFalse("pAssword1".matchesPasswordRegex())
    }
}
