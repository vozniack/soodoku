package dev.vozniack.soodoku.core.domain.extension

import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.flatLocks
import dev.vozniack.soodoku.lib.extension.status
import java.time.LocalDateTime

fun Soodoku.toGame(user: User? = null, difficulty: Difficulty, hints: Int): Game = status().let {
    Game(
        initialBoard = it.board.flatBoard(),
        solvedBoard = it.solved.flatBoard(),
        currentBoard = it.board.flatBoard(),
        locks = it.locks.flatLocks(),
        difficulty = difficulty,
        hints = hints,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        user = user
    )
}

fun Game.toSoodoku(): Soodoku = Soodoku(currentBoard, solvedBoard, locks)

fun Game.parseNotes(): MutableMap<Pair<Int, Int>, List<String>> =
    notes?.takeIf { it.isNotBlank() }?.split(";")?.mapNotNull { entry ->
        val parts = entry.split(",")
        if (parts.size < 2) return@mapNotNull null

        val row = parts[0].toIntOrNull() ?: return@mapNotNull null
        val col = parts[1].toIntOrNull() ?: return@mapNotNull null
        val values = parts.drop(2)

        Pair(row to col, values)
    }?.toMap()?.toMutableMap() ?: mutableMapOf()

fun Map<Pair<Int, Int>, List<String>>.serializeNotes(): String = entries.joinToString(";") { (pos, values) ->
    "${pos.first},${pos.second}," + values.joinToString(",")
}
