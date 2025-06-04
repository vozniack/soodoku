package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.exception.SoodokuMappingException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SoodokuMapperTest {

    @Test
    fun `map board from flat board`() {
        val flatBoard = "461859327723146859589732461197385642352467918648291735975614283236578194814923576"
        val board: Array<IntArray> = flatBoard.mapBoard()

        assertEquals(board.flatBoard(), flatBoard)
    }

    @Test
    fun `map board from incorrect flat board`() {
        val flatBoard = "a6185932772314685958973246119738564235246791864829173597561428323657819481492357b"

        assertFailsWith<SoodokuMappingException> {
            flatBoard.mapBoard()
        }
    }

    @Test
    fun `map board to flat board`() {
        val board: Array<IntArray> = arrayOf(
            intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            intArrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            intArrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            intArrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )

        val flatBoard: String = board.flatBoard()

        assertTrue(board.deepEquals(flatBoard.mapBoard()))
    }

    private fun Array<IntArray>.deepEquals(other: Array<IntArray>): Boolean {
        if (this.size != other.size) return false

        for (i in indices) {
            if (!this[i].contentEquals(other[i])) return false
        }

        return true
    }
}
