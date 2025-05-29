package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku

internal fun Soodoku.missingCells(): Int = board.sumOf { row -> row.count { it == 0 } }
