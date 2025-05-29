package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku

internal fun Soodoku.generate(difficulty: Soodoku.Difficulty): Soodoku {

    fun fill(): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (board[row][col] == 0) {
                    val values = (1..9).shuffled()

                    for (value in values) {
                        if (isMoveValid(row, col, value)) {
                            board[row][col] = value

                            if (fill()) {
                                return true
                            }

                            board[row][col] = 0
                        }
                    }

                    return false
                }
            }
        }

        return true
    }

    fill()

    cleanCells(difficulty)
    saveLockCells()

    return this
}

private fun Soodoku.cleanCells(difficulty: Soodoku.Difficulty): Soodoku {
    val cells = (0 until 81).shuffled()
    var removed = 0

    for (cell in cells) {
        if (removed >= difficulty.emptyCells) {
            break
        }

        val row = cell / 9
        val col = cell % 9
        val backup = board[row][col]

        if (backup != 0) {
            board[row][col] = 0

            if (hasUniqueSolution()) {
                removed++
            } else {
                board[row][col] = backup
            }
        }
    }

    return this
}

private fun Soodoku.hasUniqueSolution(): Boolean {
    val copy = Array(9) { board[it].clone() }
    var count = 0

    fun solve(): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (copy[row][col] == 0) {
                    for (value in 1..9) {
                        if (Soodoku(copy, lock).isMoveValid(row, col, value)) {
                            copy[row][col] = value

                            if (solve()) {
                                if (count++ > 1) return true
                            }

                            copy[row][col] = 0
                        }
                    }

                    return false
                }
            }
        }

        count++

        return false
    }

    solve()

    return count == 1
}

private fun Soodoku.saveLockCells() {
    lock.addAll(board.withIndex().flatMap { (row, cols) ->
        cols.withIndex().filter { it.value != 0 }.map { Pair(row, it.index) }
    })
}
