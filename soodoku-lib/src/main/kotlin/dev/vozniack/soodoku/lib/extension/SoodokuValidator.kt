package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.exception.SoodokuMoveException

internal fun Soodoku.isMoveValid(row: Int, col: Int, value: Int): Boolean {
    if (board[row].contains(value)) {
        return false
    }

    if (board.any { it[col] == value }) {
        return false
    }

    val startRow = row / 3 * 3
    val startCol = col / 3 * 3

    return (startRow until startRow + 3).all { r ->
        (startCol until startCol + 3).all { c ->
            board[r][c] != value
        }
    }
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
