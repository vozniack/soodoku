package dev.vozniack.soodoku.core.domain.entity.extension

import dev.vozniack.soodoku.core.domain.entity.Game

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
