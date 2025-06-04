package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku

internal fun Soodoku.missingCells(): Int = board.sumOf { row -> row.count { it == 0 } }
