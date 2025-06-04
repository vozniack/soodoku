package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku

fun Soodoku.solve() {

    fun solveRecursively(): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (board[row][col] == 0) {
                    for (value in 1..9) {
                        if (isMoveValid(row, col, value)) {
                            board[row][col] = value

                            if (solveRecursively()) {
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

    solveRecursively()
}
