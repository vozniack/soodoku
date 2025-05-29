package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.Soodoku

fun Soodoku.print() {
    for (row in board.indices) {
        if (row % 3 == 0 && row != 0) {
            println("------+-------+------")
        }

        for (col in board[row].indices) {
            if (col % 3 == 0 && col != 0) {
                print("| ")
            }

            val value = board[row][col]
            print(if (value == 0) ". " else "$value ")
        }
        println()
    }
}
