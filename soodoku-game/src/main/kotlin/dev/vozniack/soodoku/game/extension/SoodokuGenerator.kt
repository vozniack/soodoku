package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku

internal fun Soodoku.generate(difficulty: Soodoku.Difficulty): Soodoku = apply {

    fun fillBoard(): Boolean = (0..8).flatMap { row -> (0..8).map { col -> row to col } }
        .firstOrNull { (row, col) -> board[row][col] == 0 }
        ?.let { (row, col) ->
            (1..9).shuffled().any { value ->
                if (isMoveValid(row, col, value)) {
                    board[row][col] = value

                    if (fillBoard()) {
                        return@let true
                    }

                    board[row][col] = 0
                }

                false
            }.also { if (!it) return@also }
        } ?: true

    fillBoard()
    cleanCells(difficulty)
    saveLockCells()
}

private fun Soodoku.cleanCells(difficulty: Soodoku.Difficulty): Soodoku = apply {
    var removed = 0

    (0 until 81).shuffled()
        .map { it / 9 to it % 9 }
        .filter { (row, col) -> board[row][col] != 0 }
        .forEach { (row, col) ->
            if (removed >= difficulty.emptyCells) {
                return@forEach
            }

            val backup = board[row][col]
            board[row][col] = 0

            if (hasUniqueSolution()) {
                removed++
            } else {
                board[row][col] = backup
            }
        }
}

private fun Soodoku.hasUniqueSolution(): Boolean {
    val copy = Array(9) { board[it].clone() }
    var count = 0

    fun solve(): Boolean {
        for (i in 0 until 81) {
            val row = i / 9
            val col = i % 9

            if (copy[row][col] == 0) {
                for (value in 1..9) {
                    if (Soodoku(copy, lock).isMoveValid(row, col, value)) {
                        copy[row][col] = value

                        if (solve()) {
                            return true
                        }

                        copy[row][col] = 0
                    }
                }

                return false
            }
        }

        return ++count > 1
    }

    solve()
    return count == 1
}

private fun Soodoku.saveLockCells() {
    lock.addAll(board.withIndex().flatMap { (row, cols) ->
        cols.withIndex().filter { it.value != 0 }.map { Pair(row, it.index) }
    })
}
