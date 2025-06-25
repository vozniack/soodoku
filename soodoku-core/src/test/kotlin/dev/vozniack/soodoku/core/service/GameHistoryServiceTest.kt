package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameHistory
import dev.vozniack.soodoku.core.domain.extension.end
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.GameHistoryRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.fixture.findEmptyCell
import dev.vozniack.soodoku.core.fixture.mockGameHistory
import dev.vozniack.soodoku.core.fixture.mockMoveRequestDto
import dev.vozniack.soodoku.core.fixture.mockNewGameRequestDto
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.lib.Soodoku
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class GameHistoryServiceTest @Autowired constructor(
    private val gameHistoryService: GameHistoryService,
    private val gameService: GameService,
    private val gameRepository: GameRepository,
    private val gameHistoryRepository: GameHistoryRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @AfterEach
    fun `clean up`() {
        gameHistoryRepository.deleteAll()
        gameRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `get history when no filter is applied`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, game1, difficulty = Difficulty.EASY, duration = 1000, victory = true),
                mockGameHistory(user, game2, difficulty = Difficulty.HARD, duration = 2000, victory = false)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameHistoryService.get(null, null, pageable)

        assertEquals(2, result.content.size)
        assertEquals(1000, result.content[0].duration)
        assertEquals(2000, result.content[1].duration)
    }

    @Test
    fun `get history with filtering by difficulty`() {
        val user = userRepository.save(mockUser())

        val gameEasy = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val gameHard = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, gameEasy, difficulty = Difficulty.EASY, duration = 1000),
                mockGameHistory(user, gameHard, difficulty = Difficulty.HARD, duration = 2000)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameHistoryService.get(Difficulty.EASY, null, pageable)

        assertEquals(1, result.content.size)
        assertEquals(Difficulty.EASY, result.content[0].difficulty)
    }

    @Test
    fun `get history with filtering by victory`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, game1, victory = true),
                mockGameHistory(user, game2, victory = false)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameHistoryService.get(null, true, pageable)

        assertEquals(1, result.content.size)
        assertTrue(result.content.all { it.victory })
    }

    @Test
    fun `get history with sorting by total moves descending`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, game1, totalMoves = 10),
                mockGameHistory(user, game2, totalMoves = 20)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "totalMoves"))

        val result = gameHistoryService.get(null, null, pageable)

        assertEquals(2, result.content.size)
        assertTrue(result.content[0].totalMoves > result.content[1].totalMoves)
    }

    @Test
    fun `save history`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto())
        gameDto.simulateMoves()

        var game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024) // sleep to increase game duration

        game = gameRepository.save(game.end())

        Thread.sleep(128)

        gameHistoryService.save(game)

        assertEquals(1, gameHistoryRepository.count())

        val gameHistory: GameHistory = gameHistoryRepository.findAll().first()

        assertEquals(user.id, gameHistory.user.id)
        assertEquals(game.id, gameHistory.game.id)

        assertEquals(game.difficulty, gameHistory.difficulty)

        assertTrue(gameHistory.duration > 0)

        assertEquals(38, gameHistory.missingCells)
        assertEquals(5, gameHistory.totalMoves)
        assertEquals(1, gameHistory.usedHints)

        assertFalse(gameHistory.victory)

        assertEquals(game.startedAt.truncate(), gameHistory.startedAt.truncate())
        assertEquals(game.finishedAt!!.truncate(), gameHistory.finishedAt.truncate())
    }

    @Test
    fun `save history for not finished game`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto())
        gameDto.simulateMoves()

        val game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024)

        gameHistoryService.save(game)

        assertEquals(0, gameHistoryRepository.count())
    }

    @Test
    fun `save history for anonymous game`() {
        val gameDto = gameService.new(mockNewGameRequestDto())
        gameDto.simulateMoves()

        var game: Game = gameRepository.findById(gameDto.id).orElse(null)
        assertNotNull(game)

        Thread.sleep(1024)

        game = gameRepository.save(game.apply {
            finishedAt = LocalDateTime.now()
        })

        gameHistoryService.save(game)
    }

    private fun GameDto.simulateMoves() {
        val (row, col) = findEmptyCell()

        gameService.move(this.id, mockMoveRequestDto(row, col, 5))
        gameService.move(this.id, mockMoveRequestDto(row, col, 0))
        gameService.revert(this.id)
        gameService.move(this.id, mockMoveRequestDto(row, col, 3))
        gameService.hint(this.id)
    }

    private fun LocalDateTime.truncate(): LocalDateTime = truncatedTo(ChronoUnit.SECONDS)
}
