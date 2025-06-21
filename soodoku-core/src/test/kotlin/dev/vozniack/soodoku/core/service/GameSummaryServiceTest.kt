package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameSummary
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.GameSummaryRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.mock.mockMoveRequestDto
import dev.vozniack.soodoku.core.mock.mockNewGameRequestDto
import dev.vozniack.soodoku.core.mock.mockUser
import java.time.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

class GameSummaryServiceTest @Autowired constructor(
    private val gameSummaryService: GameSummaryService,
    private val gameService: GameService,
    private val gameRepository: GameRepository,
    private val gameSummaryRepository: GameSummaryRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up after`() {
        gameSummaryRepository.deleteAll()
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `summarize game`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto())

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 5))
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 0))
        gameService.revert(gameDto.id)
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 3))
        gameService.hint(gameDto.id)

        var game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024)

        game = gameRepository.save(game.apply {
            finishedAt = LocalDateTime.now()
        })

        Thread.sleep(128)

        gameSummaryService.summarize(game)

        assertEquals(1, gameSummaryRepository.count())

        val gameSummary: GameSummary = gameSummaryRepository.findAll().first()

        assertEquals(user.id, gameSummary.user.id)
        assertEquals(game.id, gameSummary.game.id)

        assertEquals(game.difficulty, gameSummary.difficulty)

        assertTrue(gameSummary.duration > 0)

        assertEquals(38, gameSummary.missingCells)
        assertEquals(5, gameSummary.totalMoves)
        assertEquals(1, gameSummary.usedHints)

        assertFalse(gameSummary.victory)
    }

    @Test
    fun `summarize not finished game`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto())

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 5))
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 0))
        gameService.revert(gameDto.id)
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 3))
        gameService.hint(gameDto.id)

        val game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024)

        gameSummaryService.summarize(game)

        assertEquals(0, gameSummaryRepository.count())
    }

    @Test
    fun `summarize anonymous game`() {
        val gameDto = gameService.new(mockNewGameRequestDto())

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 5))
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 0))
        gameService.revert(gameDto.id)
        gameService.move(gameDto.id, mockMoveRequestDto(row, col, 3))
        gameService.hint(gameDto.id)

        var game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024)

        game = gameRepository.save(game.apply {
            finishedAt = LocalDateTime.now()
        })

        gameSummaryService.summarize(game)
    }
}
