package dev.vozniack.soodoku.core.domain.extension

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.GameType
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.flatLocks
import dev.vozniack.soodoku.lib.extension.status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameExtensionTest : AbstractUnitTest() {

    @Test
    fun `parse soodoku status to game`() {
        val user: User = mockUser()

        val soodoku = Soodoku(Soodoku.Difficulty.EASY)
        val status: Soodoku.Status = soodoku.status()

        val game: Game = soodoku.toGame(user = user, type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3)

        assertEquals(status.board.flatBoard(), game.initialBoard)
        assertEquals(status.solved.flatBoard(), game.solvedBoard)
        assertEquals(status.board.flatBoard(), game.currentBoard)
        assertEquals(status.locks.flatLocks(), game.locks)
        assertEquals(Difficulty.EASY, game.difficulty)
        assertEquals(3, game.hints)
        assertEquals(user, game.user)
    }

    @Test
    fun `parse game to soodoku`() {
        val game: Game = Soodoku(Soodoku.Difficulty.EASY).toGame(
            type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3
        )

        val soodoku: Soodoku = game.toSoodoku()

        assertNotNull(soodoku)
    }

    @Test
    fun `parse notes when notes are null`() {
        val game: Game = Soodoku(Soodoku.Difficulty.EASY).toGame(
            type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3
        ).apply { notes = null }

        val parsed = game.parseNotes()

        assertTrue(parsed.isEmpty())
    }

    @Test
    fun `parse notes when notes are blank`() {
        val game: Game = Soodoku(Soodoku.Difficulty.EASY).toGame(
            type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3
        ).apply { notes = "   " }

        val parsed = game.parseNotes()

        assertTrue(parsed.isEmpty())
    }

    @Test
    fun `parse correct notes`() {
        val game: Game = Soodoku(Soodoku.Difficulty.EASY).toGame(
            type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3
        ).apply { notes = "1,2,+3,-5;+4,7,+1" }

        val parsed = game.parseNotes()

        assertEquals(2, parsed.size)
        assertEquals(listOf("+3", "-5"), parsed[1 to 2])
        assertEquals(listOf("+1"), parsed[4 to 7])
    }

    @Test
    fun `parse correct and malformed notes`() {
        val game: Game = Soodoku(Soodoku.Difficulty.EASY).toGame(
            type = GameType.RANDOM, difficulty = Difficulty.EASY, hints = 3
        ).apply { notes = "1,2,+3,-5;bad,entry;4,notANumber,+1" }

        val parsed = game.parseNotes()

        assertEquals(1, parsed.size)
        assertEquals(listOf("+3", "-5"), parsed[1 to 2])
    }

    @Test
    fun `serialized correct notes`() {
        val notes = mapOf(
            1 to 2 to listOf("+3", "-5"),
            4 to 7 to listOf("+1")
        )

        val result = notes.serializeNotes()
        val expected1 = "1,2,+3,-5;4,7,+1"
        val expected2 = "4,7,+1;1,2,+3,-5"

        assertTrue(result == expected1 || result == expected2)
    }

    @Test
    fun `serialize empty notes`() {
        val notes = emptyMap<Pair<Int, Int>, List<String>>()
        val result = notes.serializeNotes()

        assertEquals("", result)
    }
}
