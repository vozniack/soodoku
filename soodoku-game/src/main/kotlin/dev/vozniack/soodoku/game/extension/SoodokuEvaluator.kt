package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku

internal fun Soodoku.findConflicts(): List<Soodoku.Conflict> =
    findRowConflicts() + findColConflicts() + findBoxConflicts()

private fun Soodoku.findRowConflicts(): List<Soodoku.Conflict> = (0..8).flatMap { row ->
    board[row].withIndex()
        .filter { it.value != 0 }
        .groupBy({ it.value }, { it.index })
        .toConflicts(Soodoku.Conflict.Type.ROW, row) { col -> row to col }
}

private fun Soodoku.findColConflicts(): List<Soodoku.Conflict> = (0..8).flatMap { col ->
    (0..8).map { row -> row to board[row][col] }
        .filter { it.second != 0 }
        .groupBy({ it.second }, { it.first })
        .toConflicts(Soodoku.Conflict.Type.COL, col) { row -> row to col }
}

private fun Soodoku.findBoxConflicts(): List<Soodoku.Conflict> = (0..2).flatMap { boxRow ->
    (0..2).flatMap { boxCol ->
        val index = boxRow * 3 + boxCol

        (0..2).flatMap { r ->
            (0..2).map { c ->
                val row = boxRow * 3 + r
                val col = boxCol * 3 + c

                Triple(row, col, board[row][col])
            }
        }.filter { it.third != 0 }
            .groupBy({ it.third }, { it.first to it.second })
            .toConflicts(Soodoku.Conflict.Type.BOX, index) { it }
    }
}

private inline fun <K> Map<Int, List<K>>.toConflicts(
    type: Soodoku.Conflict.Type, index: Int, cellMapper: (K) -> Pair<Int, Int>
): List<Soodoku.Conflict> = filterValues { it.size > 1 }
    .map { (value, group) -> Soodoku.Conflict(type, value, index, group.map(cellMapper)) }
