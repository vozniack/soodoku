package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.Soodoku

fun Soodoku.print() {
    board.forEachIndexed { rowIdx, row ->
        if (rowIdx != 0 && rowIdx % 3 == 0) {
            println("------+-------+------")
        }

        println(row.mapIndexed { colIdx, value ->
            val cell = if (value == 0) "." else value.toString()
            if (colIdx != 0 && colIdx % 3 == 0) "| $cell" else cell
        }.joinToString(" "))
    }
}
