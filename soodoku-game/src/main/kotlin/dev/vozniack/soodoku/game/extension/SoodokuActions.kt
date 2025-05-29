package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku
import dev.vozniack.soodoku.game.Soodoku.Status

fun Soodoku.status(): Status = Status(board.flatBoard(), lock, missingCells(), findConflicts(), missingCells() == 0)

fun Soodoku.move(row: Int, col: Int, value: Int): Status {
    if (isMoveAllowed(row, col, value)) {
        board[row][col] = value
    }

    return status()
}
