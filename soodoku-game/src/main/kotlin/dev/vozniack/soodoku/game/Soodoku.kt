package dev.vozniack.soodoku.game

import dev.vozniack.soodoku.game.extension.mapBoard
import dev.vozniack.soodoku.game.extension.generate

class Soodoku {

    internal var board: Array<IntArray> = Array(9) { IntArray(9) { 0 } }
    internal val lock: MutableList<Pair<Int, Int>> = mutableListOf()

    constructor(difficulty: Difficulty) {
        generate(difficulty)
    }

    constructor(board: String, lock: List<Pair<Int, Int>>) {
        this.board = board.mapBoard()
        this.lock.addAll(lock)
    }

    constructor(board: Array<IntArray>, lock: List<Pair<Int, Int>>) {
        this.board = board
        this.lock.addAll(lock)
    }

    data class Conflict(
        val type: Type,
        val value: Int,
        val index: Int,

        val cells: List<Pair<Int, Int>>
    ) {
        enum class Type {
            ROW, COL, BOX
        }
    }

    data class Status(
        val board: String,
        val lock: List<Pair<Int, Int>>,

        val missingCells: Int,
        val conflicts: List<Conflict>,

        val done: Boolean
    )

    enum class Difficulty(val emptyCells: Int) {
        EASY(40), MEDIUM(45), HARD(50)
    }
}
