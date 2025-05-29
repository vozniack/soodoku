package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku
import dev.vozniack.soodoku.game.exception.SoodokuMoveException

internal fun Soodoku.isMoveValid(row: Int, col: Int, value: Int): Boolean {
    for (i in 0..8) {
        if (board[row][i] == value || board[i][col] == value) {
            return false
        }
    }

    val startRow = row / 3 * 3
    val startCol = col / 3 * 3

    for (r in startRow until startRow + 3) {
        for (c in startCol until startCol + 3) {
            if (board[r][c] == value) {
                return false
            }
        }
    }

    return true
}

internal fun Soodoku.isMoveAllowed(row: Int, col: Int, value: Int): Boolean {
    if (row !in 0..8 || col !in 0..8) {
        throw SoodokuMoveException("Row and column must be between 0 and 8")
    }

    if (value !in 0..9) {
        throw SoodokuMoveException("Value must be between 0 and 9 (0 means empty)")
    }

    if ((row to col) in lock) {
        throw SoodokuMoveException("Cannot modify a locked (pre-filled) cell at ($row, $col)")
    }

    return true
}
