package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.Soodoku.Status
import dev.vozniack.soodoku.lib.exception.SoodokuMappingException

fun Soodoku.status(): Status = Status(board, locks, missingCells(), findConflicts(), missingCells() == 0)

fun Soodoku.value(row: Int, col: Int): Int = try {
    board[row][col]
} catch (exception: IndexOutOfBoundsException) {
    throw SoodokuMappingException("Row $row and column $col are incorrect")
}

fun Soodoku.move(row: Int, col: Int, value: Int): Status {
    if (isMoveAllowed(row, col, value)) {
        board[row][col] = value
    }

    return status()
}
