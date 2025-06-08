package dev.vozniack.soodoku.core.extension

import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.lib.Soodoku

fun Game.toSoodoku(): Soodoku = Soodoku(currentBoard, locks)
