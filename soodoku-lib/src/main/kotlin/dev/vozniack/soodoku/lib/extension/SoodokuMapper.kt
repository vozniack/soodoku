package dev.vozniack.soodoku.lib.extension

import dev.vozniack.soodoku.lib.exception.SoodokuMappingException

fun String.mapBoard(): Array<IntArray> = try {
    Array(9) { row -> IntArray(9) { col -> this[row * 9 + col].digitToInt() } }
} catch (exception: Exception) {
    throw SoodokuMappingException("Flat board is in incorrect format")
}

fun Array<IntArray>.flatBoard(): String = joinToString(separator = "") { row ->
    row.joinToString(separator = "") { it.toString() }
}

fun String.mapLocks(): List<Pair<Int, Int>> = try {
    takeIf { isNotBlank() }?.let {
        split(";").map { pairStr ->
            val parts = pairStr.split(",")
            if (parts.size != 2) throw IllegalArgumentException("Incorrect pair format")
            val first = parts[0].toInt()
            val second = parts[1].toInt()
            Pair(first, second)
        }
    } ?: emptyList()
} catch (e: Exception) {
    throw SoodokuMappingException("Flat locks are in incorrect format")
}

fun List<Pair<Int, Int>>.flatLocks(): String = this.joinToString(separator = ";") { "${it.first},${it.second}" }
