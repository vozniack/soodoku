package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.defaultBoard
import dev.vozniack.soodoku.lib.defaultLocks
import dev.vozniack.soodoku.lib.defaultSolved
import dev.vozniack.soodoku.lib.exception.SoodokuMoveException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SoodokuValidatorTest {

    @Test
    fun `check if move is valid`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        assertTrue(soodoku.isMoveValid(0, 2, 6))
        assertFalse(soodoku.isMoveValid(0, 2, 5))
        assertFalse(soodoku.isMoveValid(0, 2, 7))
    }

    @Test
    fun `check if move is allowed`() {
        val soodoku = Soodoku(defaultBoard(), defaultSolved(), defaultLocks())

        soodoku.move(0, 2, 6)

        // Incorrect columns

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 9, 0)
        }

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, -1, 0)
        }

        // Incorrect rows

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(9, 0, 0)
        }

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(-1, 0, 0)
        }

        // Incorrect values

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 2, -1)
        }

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 2, 10)
        }

        // Locked field

        assertFailsWith<SoodokuMoveException> {
            soodoku.move(0, 0, 3)
        }
    }
}
