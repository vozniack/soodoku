package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.GameDto

fun GameDto.findEmptyCell(): Pair<Int, Int> = board
    .withIndex()
    .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
    .first { it.third == 0 }
    .let { it.first to it.second }
