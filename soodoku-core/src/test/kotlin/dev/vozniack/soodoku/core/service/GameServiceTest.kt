package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.MoveDto
import dev.vozniack.soodoku.core.api.dto.NewGameDto
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.MoveRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.domain.types.MoveType
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.lib.extension.flatBoard
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class GameServiceTest @Autowired constructor(
    private val gameService: GameService,
    private val gameRepository: GameRepository,
    private val moveRepository: MoveRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get game with anonymous user`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

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
    fun `create new game with anonymous user`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

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

        assertEquals(0, gameDto.moves)
        assertEquals(0, gameDto.realMoves)

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

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

        assertEquals(0, gameDto.moves)
        assertEquals(0, gameDto.realMoves)

        assertNotNull(gameDto.createdAt)
        assertNull(gameDto.updatedAt)
        assertNull(gameDto.finishedAt)
    }

    @Test
    fun `make a move with anonymous user`() {
        val initialGameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(initialGameDto.id, MoveDto(row = row, col = col, value = 5))

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(initialGameDto.id, updatedGameDto.id)
        assertNull(updatedGameDto.userId)

        assertEquals(initialGameDto.missing - 1, updatedGameDto.missing)

        assertEquals(1, updatedGameDto.moves)
        assertEquals(1, updatedGameDto.realMoves)

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

        val initialGameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(initialGameDto.id, MoveDto(row = row, col = col, value = 5))

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(initialGameDto.id, updatedGameDto.id)
        assertEquals(user.id, updatedGameDto.userId)

        assertEquals(initialGameDto.missing - 1, updatedGameDto.missing)

        assertEquals(1, updatedGameDto.moves)
        assertEquals(1, updatedGameDto.realMoves)

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

        val initialGameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.move(initialGameDto.id, MoveDto(row = row, col = col, value = 5))
        }
    }

    @Test
    fun `make a move when game is finished`() {
        val initialGameDto = gameService.new(NewGameDto(Difficulty.EASY))

        gameRepository.findById(initialGameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        val (row, col) = initialGameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        assertThrows<ConflictException> {
            gameService.move(initialGameDto.id, MoveDto(row = row, col = col, value = 5))
        }
    }

    @Test
    fun `make a move for incorrect cell`() {
        val initialGameDto = gameService.new(NewGameDto(Difficulty.EASY))

        assertThrows<ConflictException> {
            gameService.move(initialGameDto.id, MoveDto(row = 9, col = 1, value = 5))
        }
    }

    @Test
    fun `revert last move with anonymous user`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves)
        assertEquals(1, updatedGameDto.realMoves)
        assertEquals(5, updatedGameDto.board[row][col])

        val revertedGameDto = gameService.revert(gameDto.id)

        assertEquals(gameDto.id, revertedGameDto.id)
        assertEquals(0, revertedGameDto.board[row][col])
        assertEquals(updatedGameDto.missing + 1, revertedGameDto.missing)

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(2, revertedGameDto.moves)
        assertEquals(0, revertedGameDto.realMoves)

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves)
        assertEquals(5, updatedGameDto.board[row][col])

        val revertedGameDto = gameService.revert(gameDto.id)

        assertEquals(gameDto.id, revertedGameDto.id)
        assertEquals(0, revertedGameDto.board[row][col])
        assertEquals(updatedGameDto.missing + 1, revertedGameDto.missing)

        val savedGame = gameRepository.findById(updatedGameDto.id).orElse(null)
        assertNotNull(savedGame)

        assertEquals(2, revertedGameDto.moves)
        assertEquals(0, revertedGameDto.realMoves)

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row, col, 5))

        assertEquals(1, updatedGameDto.moves)
        assertEquals(5, updatedGameDto.board[row][col])

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `undo last move when game is finished`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        gameRepository.findById(gameDto.id).ifPresent {
            gameRepository.save(it.apply { finishedAt = LocalDateTime.now() })
        }

        assertThrows<ConflictException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `undo last move when game has no moves`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        assertEquals(0, gameDto.moves)

        assertThrows<ConflictException> {
            gameService.revert(gameDto.id)
        }
    }

    @Test
    fun `end game with anonymous user`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))
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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))
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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        assertThrows<UnauthorizedException> {
            gameService.end(gameDto.id)
        }
    }

    @Test
    fun `delete game and its moves with anonymous user`() {
        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row = row, col = col, value = 5))

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row = row, col = col, value = 5))

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

        val gameDto = gameService.new(NewGameDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val updatedGameDto = gameService.move(gameDto.id, MoveDto(row = row, col = col, value = 5))

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
