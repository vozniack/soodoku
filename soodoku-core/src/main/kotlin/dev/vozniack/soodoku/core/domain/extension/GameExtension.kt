package dev.vozniack.soodoku.core.domain.extension

import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.lib.Soodoku

fun Game.toSoodoku(): Soodoku = Soodoku(currentBoard, solvedBoard, locks)
