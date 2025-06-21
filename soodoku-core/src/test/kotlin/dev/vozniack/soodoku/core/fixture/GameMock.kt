package dev.vozniack.soodoku.core.fixture

import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.NoteRequestDto
import dev.vozniack.soodoku.core.domain.types.Difficulty

fun mockNewGameRequestDto(
    difficulty: Difficulty = Difficulty.EASY
): NewGameRequestDto = NewGameRequestDto(difficulty = difficulty)

fun mockMoveRequestDto(
    row: Int = 1, col: Int = 1, value: Int = 1
): MoveRequestDto = MoveRequestDto(row = row, col = col, value = value)

fun mockNoteRequestDto(
    row: Int = 1, col: Int = 1, values: List<String> = listOf("+1", "-3", "+5")
): NoteRequestDto = NoteRequestDto(row = row, col = col, values = values.toTypedArray())
