package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.domain.extension.parseNotes
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.extension.toSoodoku
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.MoveRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.MoveType
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.mock.mockNoteRequestDto
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.lib.Soodoku
import dev.vozniack.soodoku.lib.extension.flatBoard
import dev.vozniack.soodoku.lib.extension.mapBoard
import dev.vozniack.soodoku.lib.extension.solve
import dev.vozniack.soodoku.lib.extension.status
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class GameServiceTest @Autowired constructor(
    private val gameService: GameService,
    private val gameRepository: GameRepository,
    private val moveRepository: MoveRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @BeforeEach
    fun `clear up before`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun `clean up after`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val fetchedGame = gameService.get(gameDto.id)
        assertNotNull(fetchedGame)
        assertNull(fetchedGame.userId)
    }

    @Test
    fun `get game with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val fetchedGame = gameService.get(gameDto.id)
        assertNotNull(fetchedGame)
        assertNotNull(fetchedGame.userId)
    }

    @Test
    fun `get not existing game`() {
        assertThrows<NotFoundException> {
            gameService.get(UUID.randomUUID())
        }
    }

    @Test
    fun `get last game with anonymous user`() {
        assertThrows<UnauthorizedException> {
            gameService.getLastGame()
        }
    }

    @Test
    fun `get last game with existing user`() {
        val user = userRepository.save(mockUser())
        val game = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val lastGame = gameService.getLastGame()

        assertNotNull(lastGame)
        assertEquals(game.id, lastGame!!.id)
    }

    @Test
    fun `get last game with existing user when game is finished`() {
        val user = userRepository.save(mockUser())

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val lastGame = gameService.getLastGame()

        assertNull(lastGame)
    }

    @Test
    fun `get last game with existing user when there is no game`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val lastGame = gameService.getLastGame()

        assertNull(lastGame)
    }

    @Test
    fun `get user games with anonymous user`() {
        assertThrows<UnauthorizedException> {
            gameService.getGames(true, PageRequest.of(0, 16))
        }
    }

    @Test
    fun `get finished games with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(null, Difficulty.EASY, 3)
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(null, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        val games = gameService.getGames(true, PageRequest.of(0, 16))

        assertEquals(2, games.content.size)
    }

    @Test
    fun `get not finished games with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(null, Difficulty.EASY, 3)
        )

        gameRepository.save(
            Soodoku(Soodoku.Difficulty.EASY).toGame(null, Difficulty.EASY, 3)
                .apply { finishedAt = LocalDateTime.now() }
        )

        val games = gameService.getGames(false, PageRequest.of(0, 16))

        assertEquals(1, games.content.size)
    }

    @Test
    fun `create new game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(savedGame.id, gameDto.id)
        assertNull(gameDto.userId)

        assertEquals(9, gameDto.board.size)
        assertTrue(gameDto.board.all { it.size == 9 })
        assertTrue(gameDto.locks.isNotEmpty())
        assertTrue(gameDto.conflicts.isEmpty())

        assertEquals(Difficulty.EASY.name, gameDto.difficulty.name)
        assertEquals(gameDto.board.flatBoard().count { it == '0' }, gameDto.missing)

        assertEquals(0, gameDto.moves.size)

        assertNotNull(gameDto.createdAt)
        assertNull(gameDto.updatedAt)
        assertNull(gameDto.finishedAt)
    }

    @Test
    fun `create new game with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(savedGame.id, gameDto.id)
        assertEquals(user.id, gameDto.userId)

        assertEquals(9, gameDto.board.size)
        assertTrue(gameDto.board.all { it.size == 9 })
        assertTrue(gameDto.locks.isNotEmpty())
        assertTrue(gameDto.conflicts.isEmpty())

        assertEquals(Difficulty.EASY.name, gameDto.difficulty.name)
        assertEquals(gameDto.board.flatBoard().count { it == '0' }, gameDto.missing)

        assertEquals(0, gameDto.moves.size)

        assertNotNull(gameDto.createdAt)
        assertNull(gameDto.updatedAt)
        assertNull(gameDto.finishedAt)
    }

    @Test
    fun `make a move with anonymous user`() {
        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(initialGameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(initialGameDto.id, updatedGameDto.id)
        assertNull(updatedGameDto.userId)

        assertEquals(initialGameDto.missing - 1, updatedGameDto.missing)

        assertEquals(1, updatedGameDto.moves.size)

        assertEquals(5, updatedGameDto.board[row][col])

        val move = savedGame.moves.first()

        assertEquals(row, move.row)
        assertEquals(col, move.col)
        assertEquals(0, move.before)
        assertEquals(5, move.after)
    }

    @Test
    fun `make a move with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(initialGameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(initialGameDto.id, updatedGameDto.id)
        assertEquals(user.id, updatedGameDto.userId)

        assertEquals(initialGameDto.missing - 1, updatedGameDto.missing)

        assertEquals(1, updatedGameDto.moves.size)

        assertEquals(5, updatedGameDto.board[row][col])

        val move = savedGame.moves.first()

        assertEquals(row, move.row)
        assertEquals(col, move.col)
        assertEquals(0, move.before)
        assertEquals(5, move.after)
    }

    @Test
    fun `make a move with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.move(initialGameDto.id, MoveRequestDto(row = row, col = col, value = 5))
        }
    }

    @Test
    fun `make a move when game is finished`() {
        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(initialGameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        assertThrows<ConflictException> {
            gameService.move(initialGameDto.id, MoveRequestDto(row = row, col = col, value = 5))
        }
    }

    @Test
    fun `make a move and finish the game`() {
        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.DEV_FILLED))

        val game = gameRepository.findById(initialGameDto.id).get()

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val value = game.solvedBoard.mapBoard()[row][col]

        val updatedGameDto = gameService.move(initialGameDto.id, MoveRequestDto(row = row, col = col, value = value))
        assertEquals(initialGameDto.id, updatedGameDto.id)

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)

        assertNotNull(savedGame)
        assertNotNull(savedGame.finishedAt)
    }

    @Test
    fun `make a move for incorrect cell`() {
        val initialGameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        assertThrows<ConflictException> {
            gameService.move(initialGameDto.id, MoveRequestDto(row = 9, col = 1, value = 5))
        }
    }

    @Test
    fun `revert last move with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves.size)
        assertEquals(5, updatedGameDto.board[row][col])

        val revertedGameDto = gameService.revert(gameDto.id)

        assertEquals(gameDto.id, revertedGameDto.id)
        assertEquals(0, revertedGameDto.board[row][col])
        assertEquals(updatedGameDto.missing + 1, revertedGameDto.missing)

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(2, revertedGameDto.moves.size)

        val move = savedGame.moves.first()

        assertEquals(MoveType.NORMAL, move.type)
        assertNotNull(move.revertedAt)
        assertEquals(row, move.row)
        assertEquals(col, move.col)
        assertEquals(0, move.before)
        assertEquals(5, move.after)

        val revert = savedGame.moves[1]

        assertEquals(MoveType.REVERT, revert.type)
        assertNull(revert.revertedAt)
        assertEquals(row, revert.row)
        assertEquals(col, revert.col)
        assertEquals(5, revert.before)
        assertEquals(0, revert.after)
    }

    @Test
    fun `revert last move with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves.size)
        assertEquals(5, updatedGameDto.board[row][col])

        val revertedGameDto = gameService.revert(gameDto.id)

        assertEquals(gameDto.id, revertedGameDto.id)
        assertEquals(0, revertedGameDto.board[row][col])
        assertEquals(updatedGameDto.missing + 1, revertedGameDto.missing)

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(2, revertedGameDto.moves.size)

        val move = savedGame.moves.first()

        assertEquals(MoveType.NORMAL, move.type)
        assertNotNull(move.revertedAt)
        assertEquals(row, move.row)
        assertEquals(col, move.col)
        assertEquals(0, move.before)
        assertEquals(5, move.after)

        val revert = savedGame.moves[1]

        assertEquals(MoveType.REVERT, revert.type)
        assertNull(revert.revertedAt)
        assertEquals(row, revert.row)
        assertEquals(col, revert.col)
        assertEquals(5, revert.before)
        assertEquals(0, revert.after)
    }

    @Test
    fun `revert last move with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves.size)
        assertEquals(5, updatedGameDto.board[row][col])

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `revert last move when game is finished`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        assertThrows<ConflictException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `revert last move when game has no moves`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        assertEquals(0, gameDto.moves.size)

        assertThrows<ConflictException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `make a note with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val firstNoteRow = 1
        val firstNoteCol = 3
        val firstNoteValues = listOf("+1", "-2", "+8")

        val secondNoteRow = 4
        val secondNoteCol = 7
        val secondNoteValues = listOf("+3", "-7", "+1")

        var updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(firstNoteRow, firstNoteCol, firstNoteValues)
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(secondNoteRow, secondNoteCol, secondNoteValues)
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        var savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        var notes = savedGame.parseNotes()

        assertEquals(2, notes.size)

        assertTrue(notes.containsKey(firstNoteRow to firstNoteCol))
        assertEquals(firstNoteValues, notes[firstNoteRow to firstNoteCol])

        assertTrue(notes.containsKey(secondNoteRow to secondNoteCol))
        assertEquals(secondNoteValues, notes[secondNoteRow to secondNoteCol])

        updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(secondNoteRow, secondNoteCol, listOf())
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        notes = savedGame.parseNotes()

        assertEquals(1, notes.size)

        assertTrue(notes.containsKey(firstNoteRow to firstNoteCol))
        assertEquals(firstNoteValues, notes[firstNoteRow to firstNoteCol])
    }

    @Test
    fun `make a note with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val firstNoteRow = 1
        val firstNoteCol = 3
        val firstNoteValues = listOf("+1", "-2", "+8")

        val secondNoteRow = 4
        val secondNoteCol = 7
        val secondNoteValues = listOf("+3", "-7", "+1")

        var updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(firstNoteRow, firstNoteCol, firstNoteValues)
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(secondNoteRow, secondNoteCol, secondNoteValues)
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        var savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        var notes = savedGame.parseNotes()

        assertEquals(2, notes.size)

        assertTrue(notes.containsKey(firstNoteRow to firstNoteCol))
        assertEquals(firstNoteValues, notes[firstNoteRow to firstNoteCol])

        assertTrue(notes.containsKey(secondNoteRow to secondNoteCol))
        assertEquals(secondNoteValues, notes[secondNoteRow to secondNoteCol])


        updatedGameDto = gameService.note(
            gameDto.id, mockNoteRequestDto(secondNoteRow, secondNoteCol, listOf())
        )

        assertEquals(gameDto.id, updatedGameDto.id)

        savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        notes = savedGame.parseNotes()

        assertEquals(1, notes.size)

        assertTrue(notes.containsKey(firstNoteRow to firstNoteCol))
        assertEquals(firstNoteValues, notes[firstNoteRow to firstNoteCol])
    }

    @Test
    fun `make a note with user different than owner`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.note(gameDto.id, mockNoteRequestDto(1, 1, listOf("+1", "-1")))
        }
    }

    @Test
    fun `make a note when game is finished`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        assertThrows<ConflictException> {
            gameService.note(gameDto.id, mockNoteRequestDto(1, 1, listOf("+1", "-1")))
        }
    }

    @Test
    fun `delete notes with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        gameRepository.save(
            savedGame.apply {
                notes = "1,3,+1,-2,+8;1,4,+4,+7,-1"
            }
        )

        val updatedGameDto = gameService.deleteNotes(gameDto.id)

        assertTrue(updatedGameDto.notes.isEmpty())
    }

    @Test
    fun `delete notes with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        gameRepository.save(
            savedGame.apply {
                notes = "1,3,+1,-2,+8;1,4,+4,+7,-1"
            }
        )

        val updatedGameDto = gameService.deleteNotes(gameDto.id)

        assertTrue(updatedGameDto.notes.isEmpty())
    }

    @Test
    fun `delete notes with user different than owner`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        gameRepository.save(
            savedGame.apply {
                notes = "1,3,+1,-2,+8;1,4,+4,+7,-1"
            }
        )

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.deleteNotes(gameDto.id)
        }
    }

    @Test
    fun `use hint with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val initialHints = gameDto.hints
        val flatBoardBefore = gameDto.board.flatMap { it.toList() }

        val updatedGameDto = gameService.hint(gameDto.id)

        assertEquals(gameDto.id, updatedGameDto.id)
        assertEquals(initialHints - 1, updatedGameDto.hints)
        assertEquals(1, updatedGameDto.moves.size)

        val hintMoveDto = updatedGameDto.moves.first()
        assertEquals(MoveType.HINT, hintMoveDto.type)
        assertEquals(false, hintMoveDto.reverted)

        val valueAfter = updatedGameDto.board[hintMoveDto.row][hintMoveDto.col]
        assertEquals(hintMoveDto.after, valueAfter)

        val index = hintMoveDto.row * 9 + hintMoveDto.col
        assertEquals(flatBoardBefore[index], hintMoveDto.before)

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        val hintMove = savedGame.moves.first()

        assertEquals(MoveType.HINT, hintMove.type)
        assertEquals(hintMoveDto.row, hintMove.row)
        assertEquals(hintMoveDto.col, hintMove.col)
        assertEquals(hintMoveDto.after, hintMove.after)
        assertEquals(hintMoveDto.before, hintMove.before)
    }

    @Test
    fun `use hint with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val initialHints = gameDto.hints
        val flatBoardBefore = gameDto.board.flatMap { it.toList() }

        val updatedGameDto = gameService.hint(gameDto.id)

        assertEquals(gameDto.id, updatedGameDto.id)
        assertEquals(initialHints - 1, updatedGameDto.hints)
        assertEquals(1, updatedGameDto.moves.size)

        val hintMoveDto = updatedGameDto.moves.first()
        assertEquals(MoveType.HINT, hintMoveDto.type)
        assertEquals(false, hintMoveDto.reverted)

        val valueAfter = updatedGameDto.board[hintMoveDto.row][hintMoveDto.col]
        assertEquals(hintMoveDto.after, valueAfter)

        val index = hintMoveDto.row * 9 + hintMoveDto.col
        assertEquals(flatBoardBefore[index], hintMoveDto.before)

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(savedGame)

        val hintMove = savedGame.moves.first()

        assertEquals(MoveType.HINT, hintMove.type)
        assertEquals(hintMoveDto.row, hintMove.row)
        assertEquals(hintMoveDto.col, hintMove.col)
        assertEquals(hintMoveDto.after, hintMove.after)
        assertEquals(hintMoveDto.before, hintMove.before)
    }

    @Test
    fun `use hint with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.hint(gameDto.id)
        }
    }

    @Test
    fun `use hint when game is filled`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))
        val game = gameRepository.findById(gameDto.id).orElseThrow()

        val soodoku = game.toSoodoku()
        val status = soodoku.let { it.solve(); it.status() }

        gameRepository.save(game.apply { currentBoard = status.board.flatBoard() })

        assertThrows<ConflictException> {
            gameService.hint(gameDto.id)
        }
    }

    @Test
    fun `use hint when game is finished`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        assertThrows<ConflictException> {
            gameService.hint(gameDto.id)
        }
    }

    @Test
    fun `use hint when there are no hints`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { hints = 0 })
        }

        assertThrows<ConflictException> {
            gameService.hint(gameDto.id)
        }
    }

    @Test
    fun `use hint and clean up the notes`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.DEV_FILLED))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.note(gameDto.id, mockNoteRequestDto(row, col, listOf("+1", "-2", "+8")))

        val updatedGameDto = gameService.hint(gameDto.id)
        assertEquals(gameDto.id, updatedGameDto.id)

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)

        assertNotNull(savedGame)
        assertFalse(savedGame.parseNotes().contains(row to col))
    }

    @Test
    fun `use hint and finish the game`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.DEV_FILLED))
        val updatedGameDto = gameService.hint(gameDto.id)

        assertEquals(gameDto.id, updatedGameDto.id)

        val savedGame = gameRepository.findById(gameDto.id).orElse(null)

        assertNotNull(savedGame)
        assertNotNull(savedGame.finishedAt)
    }

    @Test
    fun `end game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))
        val endedGameDto = gameService.end(gameDto.id)

        val savedGame = gameRepository.findById(endedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(gameDto.id, endedGameDto.id)

        assertNotNull(savedGame!!.finishedAt)
        assertNotNull(savedGame.updatedAt)
    }

    @Test
    fun `end game with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))
        val endedGameDto = gameService.end(gameDto.id)

        val savedGame = gameRepository.findById(endedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(gameDto.id, endedGameDto.id)

        assertNotNull(savedGame!!.finishedAt)
        assertNotNull(savedGame.updatedAt)
    }

    @Test
    fun `end game with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.end(gameDto.id)
        }
    }

    @Test
    fun `end game when game is finished`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        assertThrows<ConflictException> {
            gameService.end(gameDto.id)
        }
    }


    @Test
    fun `delete game and its moves with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val savedGameBeforeDelete = gameRepository.findById(updatedGameDto.id).orElse(null)

        assertNotNull(savedGameBeforeDelete)
        assertEquals(1, savedGameBeforeDelete.moves.size)

        gameService.delete(updatedGameDto.id)

        assertTrue(gameRepository.findById(updatedGameDto.id).isEmpty)

        assertEquals(0, moveRepository.count())
    }

    @Test
    fun `delete game and its moves with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val savedGameBeforeDelete = gameRepository.findById(updatedGameDto.id).orElse(null)

        assertNotNull(savedGameBeforeDelete)
        assertEquals(1, savedGameBeforeDelete.moves.size)

        gameService.delete(updatedGameDto.id)

        assertTrue(gameRepository.findById(updatedGameDto.id).isEmpty)

        assertEquals(0, moveRepository.count())
    }

    @Test
    fun `delete game and its moves with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val savedGameBeforeDelete = gameRepository.findById(updatedGameDto.id).orElse(null)

        assertNotNull(savedGameBeforeDelete)
        assertEquals(1, savedGameBeforeDelete.moves.size)

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.delete(updatedGameDto.id)
        }
    }
}
