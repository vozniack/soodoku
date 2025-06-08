package dev.vozniack.soodoku.lib

import dev.vozniack.soodoku.lib.extension.generate
import dev.vozniack.soodoku.lib.extension.mapBoard
import dev.vozniack.soodoku.lib.extension.mapLocks

class Soodoku {

    internal var board: Array<IntArray> = Array(9) { IntArray(9) { 0 } }
    internal var locks: MutableList<Pair<Int, Int>> = mutableListOf()

    constructor(difficulty: Difficulty) {
        generate(difficulty)
    }

    constructor(board: String, locks: String) {
        this.board = board.mapBoard()
        this.locks = locks.mapLocks().toMutableList()
    }

    constructor(board: Array<IntArray>, locks: List<Pair<Int, Int>>) {
        this.board = board
        this.locks = locks.toMutableList()
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
        val board: Array<IntArray>,
        val locks: List<Pair<Int, Int>>,

        val missingCells: Int,
        val conflicts: List<Conflict>,

        val done: Boolean
    )

    enum class Difficulty(val emptyCells: Int) {
        EASY(40), MEDIUM(45), HARD(50)
    }
}
