package dev.vozniack.soodoku.cli

import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.exception.SoodokuMoveException
import dev.vozniack.soodoku.lib.extension.move
import dev.vozniack.soodoku.lib.extension.print
import dev.vozniack.soodoku.lib.extension.solve
import dev.vozniack.soodoku.lib.extension.status
import kotlin.system.exitProcess

class SoodokuCli {
    private lateinit var soodoku: Soodoku

    fun start() {
        println("Warm welcome in Soodoku!")

        while (true) {
            println("Please select difficulty: EASY | MEDIUM | HARD")

            soodoku = Soodoku(readDifficulty())

            println("LET THE GAME BEGIN")

            play()
        }
    }

    private fun play() {
        while (true) {
            println().let { soodoku.print() }.let { println() }

            val status = soodoku.status()

            if (status.missingCells == 0) {
                println("Congratulations! You've solved the Soodoku!")
                break
            }

            if (status.conflicts.isNotEmpty()) {
                println("Conflicts found:")

                status.conflicts.forEach {
                    println(" - Conflict: ${it.type.map()} ${it.index} with value ${it.value} at cells ${it.cells}")
                }
            }

            println("Enter move (row col value) | NEW to restart | EXIT to quit:")

            when (val input = readln().trim().uppercase()) {
                "EXIT" -> {
                    println("Thanks for playing! Goodbye.")
                    exitProcess(0)
                }

                "NEW" -> {
                    println("Restarting the game...")
                    return
                }

                "SOLVE" -> {
                    println("Solving...")
                    soodoku.solve()
                }

                else -> {
                    val parts = input.split(Regex("\\s+"))

                    if (parts.size != 3) {
                        println("Invalid input. Please enter: row col value (e.g., 3 4 9)")
                        continue
                    }

                    val (rowStr, colStr, valStr) = parts

                    val row = rowStr.toIntOrNull()
                    val col = colStr.toIntOrNull()
                    val value = valStr.toIntOrNull()

                    if (row == null || col == null || value == null) {
                        println("Please enter valid integers.")
                        continue
                    }

                    try {
                        soodoku.move(row, col, value)
                    } catch (e: SoodokuMoveException) {
                        println("Move failed: ${e.message}")
                    }
                }
            }
        }
    }

    private fun readDifficulty(): Soodoku.Difficulty {
        while (true) {
            val input = readln().trim().uppercase()

            return when (input) {
                "EASY" -> Soodoku.Difficulty.EASY
                "MEDIUM" -> Soodoku.Difficulty.MEDIUM
                "HARD" -> Soodoku.Difficulty.HARD

                else -> {
                    println("Invalid difficulty. Please type EASY, MEDIUM, or HARD.")
                    continue
                }
            }
        }
    }

    private fun Soodoku.Conflict.Type.map(): String = when (this) {
        Soodoku.Conflict.Type.COL -> "column"
        Soodoku.Conflict.Type.ROW -> "row"
        Soodoku.Conflict.Type.BOX -> "box"
    }
}

fun main() {
    SoodokuCli().start()
}
