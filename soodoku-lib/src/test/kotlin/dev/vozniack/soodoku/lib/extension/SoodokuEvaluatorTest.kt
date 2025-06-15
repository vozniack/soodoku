package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import kotlin.test.Test
import kotlin.test.assertEquals

class SoodokuEvaluatorTest {

    @Test
    fun `find only row conflicts`() {
        val soodoku = Soodoku(
            arrayOf(
                intArrayOf(5, 3, 0, 3, 7, 0, 0, 0, 0),
                intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
                intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
                intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
                intArrayOf(4, 0, 0, 8, 4, 3, 0, 4, 1),
                intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
                intArrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
                intArrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
                intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
            ), arrayOf(), listOf()
        )

        val conflicts: List<Soodoku.Conflict> = soodoku.conflicts()

        assertEquals(2, conflicts.size)

        assertEquals(Soodoku.Conflict.Type.ROW, conflicts[0].type)
        assertEquals(3, conflicts[0].value)
        assertEquals(0, conflicts[0].index)
        assertEquals(2, conflicts[0].cells.size)
        assertEquals(Pair(0, 1), conflicts[0].cells[0])
        assertEquals(Pair(0, 3), conflicts[0].cells[1])

        assertEquals(Soodoku.Conflict.Type.ROW, conflicts[1].type)
        assertEquals(4, conflicts[1].value)
        assertEquals(4, conflicts[1].index)
        assertEquals(3, conflicts[1].cells.size)
        assertEquals(Pair(4, 0), conflicts[1].cells[0])
        assertEquals(Pair(4, 4), conflicts[1].cells[1])
        assertEquals(Pair(4, 7), conflicts[1].cells[2])
    }

    @Test
    fun `find only col conflicts`() {
        val soodoku = Soodoku(
            arrayOf(
                intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
                intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
                intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
                intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
                intArrayOf(6, 0, 0, 8, 7, 3, 0, 0, 1),
                intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
                intArrayOf(0, 4, 0, 0, 0, 0, 2, 8, 0),
                intArrayOf(1, 0, 0, 4, 7, 9, 0, 0, 5),
                intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
            ), arrayOf(), listOf()
        )

        val conflicts: List<Soodoku.Conflict> = soodoku.conflicts()

        assertEquals(2, conflicts.size)

        assertEquals(Soodoku.Conflict.Type.COL, conflicts[0].type)
        assertEquals(6, conflicts[0].value)
        assertEquals(0, conflicts[0].index)
        assertEquals(2, conflicts[0].cells.size)
        assertEquals(Pair(1, 0), conflicts[0].cells[0])
        assertEquals(Pair(4, 0), conflicts[0].cells[1])

        assertEquals(Soodoku.Conflict.Type.COL, conflicts[1].type)
        assertEquals(7, conflicts[1].value)
        assertEquals(4, conflicts[1].index)
        assertEquals(3, conflicts[1].cells.size)
        assertEquals(Pair(0, 4), conflicts[1].cells[0])
        assertEquals(Pair(4, 4), conflicts[1].cells[1])
        assertEquals(Pair(7, 4), conflicts[1].cells[2])
    }

    @Test
    fun `find mixed conflicts`() {
        val soodoku = Soodoku(
            arrayOf(
                intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
                intArrayOf(6, 3, 0, 1, 9, 5, 0, 0, 0),
                intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
                intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
                intArrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
                intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
                intArrayOf(0, 6, 0, 0, 0, 0, 2, 4, 0),
                intArrayOf(0, 0, 0, 4, 1, 9, 8, 8, 5),
                intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
            ), arrayOf(), listOf()
        )

        val conflicts: List<Soodoku.Conflict> = soodoku.conflicts()

        assertEquals(4, conflicts.size)

        assertEquals(Soodoku.Conflict.Type.ROW, conflicts[0].type)
        assertEquals(8, conflicts[0].value)
        assertEquals(7, conflicts[0].index)
        assertEquals(2, conflicts[0].cells.size)
        assertEquals(Pair(7, 6), conflicts[0].cells[0])
        assertEquals(Pair(7, 7), conflicts[0].cells[1])

        assertEquals(Soodoku.Conflict.Type.COL, conflicts[1].type)
        assertEquals(3, conflicts[1].value)
        assertEquals(1, conflicts[1].index)
        assertEquals(2, conflicts[1].cells.size)
        assertEquals(Pair(0, 1), conflicts[1].cells[0])
        assertEquals(Pair(1, 1), conflicts[1].cells[1])

        assertEquals(Soodoku.Conflict.Type.BOX, conflicts[2].type)
        assertEquals(3, conflicts[2].value)
        assertEquals(0, conflicts[2].index)
        assertEquals(2, conflicts[2].cells.size)
        assertEquals(Pair(0, 1), conflicts[2].cells[0])
        assertEquals(Pair(1, 1), conflicts[2].cells[1])

        assertEquals(Soodoku.Conflict.Type.BOX, conflicts[3].type)
        assertEquals(8, conflicts[3].value)
        assertEquals(8, conflicts[3].index)
        assertEquals(2, conflicts[3].cells.size)
        assertEquals(Pair(7, 6), conflicts[3].cells[0])
        assertEquals(Pair(7, 7), conflicts[3].cells[1])
    }
}
