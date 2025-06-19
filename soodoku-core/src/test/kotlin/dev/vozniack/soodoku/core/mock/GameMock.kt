package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.api.dto.NoteRequestDto

fun mockNoteRequestDto(
    row: Int = 1, col: Int = 1, values: List<String> = listOf("+1", "-3", "+5")
): NoteRequestDto = NoteRequestDto(row = row, col = col, values = values.toTypedArray())
