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

    @Test
    fun `map locks from flat locks string`() {
        val flatLocks = "1,2;3,4;5,6"
        val locks: List<Pair<Int, Int>> = flatLocks.mapLocks()

        assertEquals(listOf(Pair(1, 2), Pair(3, 4), Pair(5, 6)), locks)
    }

    @Test
    fun `map locks from empty string returns empty list`() {
        val flatLocks = ""
        val locks = flatLocks.mapLocks()

        assertTrue(locks.isEmpty())
    }

    @Test
    fun `map locks from incorrect flat locks string throws exception`() {
        val flatLocks = "1,2;3;5,6"

        assertFailsWith<SoodokuMappingException> {
            flatLocks.mapLocks()
        }
    }

    @Test
    fun `map locks to flat locks string`() {
        val locks = listOf(Pair(7, 8), Pair(9, 10))
        val flatLocks = locks.flatLocks()

        assertEquals("7,8;9,10", flatLocks)
    }

    @Test
    fun `map locks to and from string round-trip`() {
        val originalLocks = listOf(Pair(11, 12), Pair(13, 14), Pair(15, 16))
        val flatLocks = originalLocks.flatLocks()
        val mappedLocks = flatLocks.mapLocks()

        assertEquals(originalLocks, mappedLocks)
    }

    private fun Array<IntArray>.deepEquals(other: Array<IntArray>): Boolean {
        if (this.size != other.size) return false

        for (i in indices) {
            if (!this[i].contentEquals(other[i])) return false
        }

        return true
    }
}
