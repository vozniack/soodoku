package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.Soodoku.Status

fun Soodoku.status(): Status = Status(board, locks, missingCells(), findConflicts(), missingCells() == 0)

fun Soodoku.move(row: Int, col: Int, value: Int): Status {
    if (isMoveAllowed(row, col, value)) {
        board[row][col] = value
    }

    return status()
}
