package dev.vozniack.soodoku.lib

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

internal fun defaultSolved(): Array<IntArray> = arrayOf(
    intArrayOf(2, 4, 6, 7, 3, 9, 1, 5, 8),
    intArrayOf(5, 7, 3, 4, 1, 8, 2, 9, 6),
    intArrayOf(8, 1, 9, 6, 2, 5, 3, 7, 4),
    intArrayOf(6, 5, 8, 9, 7, 1, 4, 2, 3),
    intArrayOf(1, 2, 4, 3, 5, 6, 7, 8, 9),
    intArrayOf(3, 9, 7, 8, 4, 2, 5, 6, 1),
    intArrayOf(4, 8, 1, 5, 9, 7, 6, 3, 2),
    intArrayOf(9, 3, 5, 2, 6, 4, 8, 1, 7),
    intArrayOf(7, 6, 2, 1, 8, 3, 9, 4, 5)
)

internal fun defaultFlatSolved(): String =
    "246739158573418296819625374658971423124356789397842561481597632935264817762183945"

internal fun defaultLocks(): List<Pair<Int, Int>> = listOf(
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

internal fun defaultFlatLocks(): String =
    "0,0;0,1;0,4;0,5;0,6;0,7;0,8;1,0;1,1;1,2;1,3;1,5;1,6;1,7;2,0;2,1;2,2;2,3;2,5;2,7;2,8;3,0;3,2;3,3;3,7;3,8;4,1;4,4;4,5;4,6;4,8;5,1;5,3;5,4;5,6;5,7;5,8;6,0;6,2;6,3;6,4;6,6;6,7;7,1;7,2;7,3;7,4;7,5;7,6;7,7;7,8;8,0;8,1;8,2;8,4;8,5;8,7"
