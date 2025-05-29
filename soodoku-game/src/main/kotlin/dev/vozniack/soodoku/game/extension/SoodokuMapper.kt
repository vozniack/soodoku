package dev.vozniack.soodoku.game.extension

import dev.vozniack.soodoku.game.exception.SoodokuMappingException

internal fun String.mapBoard(): Array<IntArray> = try {
    Array(9) { row -> IntArray(9) { col -> this[row * 9 + col].digitToInt() } }
} catch (exception: Exception) {
    throw SoodokuMappingException("Flat board is in incorrect format")
}

internal fun Array<IntArray>.flatBoard(): String = joinToString(separator = "") { row ->
    row.joinToString(separator = "") { it.toString() }
}
