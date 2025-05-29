package dev.vozniack.soodoku.game

internal fun defaultBoard(): Array<IntArray> = arrayOf(
    intArrayOf(2, 4, 0, 0, 3, 9, 1, 5, 8),
    intArrayOf(5, 7, 3, 4, 0, 8, 2, 9, 0),
    intArrayOf(8, 1, 9, 6, 0, 5, 0, 7, 4),
    intArrayOf(6, 0, 8, 9, 0, 0, 0, 2, 3),
    intArrayOf(0, 2, 0, 0, 5, 6, 7, 0, 9),
    intArrayOf(0, 9, 0, 8, 4, 0, 5, 6, 1),
    intArrayOf(4, 0, 1, 5, 9, 0, 6, 3, 0),
    intArrayOf(0, 3, 5, 2, 6, 4, 8, 1, 7),
    intArrayOf(7, 6, 2, 0, 8, 3, 0, 4, 0)
)

internal fun defaultFlatBoard(): String =
    "240039158573408290819605074608900023020056709090840561401590630035264817762083040"

internal fun defaultLock(): List<Pair<Int, Int>> = listOf(
    0 to 0, 0 to 1, 0 to 4, 0 to 5, 0 to 6, 0 to 7, 0 to 8,
    1 to 0, 1 to 1, 1 to 2, 1 to 3, 1 to 5, 1 to 6, 1 to 7,
    2 to 0, 2 to 1, 2 to 2, 2 to 3, 2 to 5, 2 to 7, 2 to 8,
    3 to 0, 3 to 2, 3 to 3, 3 to 7, 3 to 8,
    4 to 1, 4 to 4, 4 to 5, 4 to 6, 4 to 8,
    5 to 1, 5 to 3, 5 to 4, 5 to 6, 5 to 7, 5 to 8,
    6 to 0, 6 to 2, 6 to 3, 6 to 4, 6 to 6, 6 to 7,
    7 to 1, 7 to 2, 7 to 3, 7 to 4, 7 to 5, 7 to 6, 7 to 7, 7 to 8,
    8 to 0, 8 to 1, 8 to 2, 8 to 4, 8 to 5, 8 to 7
)
