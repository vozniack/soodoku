package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.AbstractUnitTest
import dev.vozniack.soodoku.core.domain.entity.Game
import dev.vozniack.soodoku.core.domain.entity.GameSummary
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.GameSummaryRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.mock.mockGameSummary
import dev.vozniack.soodoku.core.mock.mockMoveRequestDto
import dev.vozniack.soodoku.core.mock.mockNewGameRequestDto
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.lib.Soodoku
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder

class GameSummaryServiceTest @Autowired constructor(
    private val gameSummaryService: GameSummaryService,
    private val gameService: GameService,
    private val gameRepository: GameRepository,
    private val gameSummaryRepository: GameSummaryRepository,
    private val userRepository: UserRepository
) : AbstractUnitTest() {

    @BeforeEach
    fun `clean up before`() {
        gameSummaryRepository.deleteAll()
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun `clean up after`() {
        gameSummaryRepository.deleteAll()
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get all summaries when no filter is applied`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameSummaryRepository.saveAll(
            listOf(
                mockGameSummary(user, game1, difficulty = Difficulty.EASY, duration = 1000, victory = true),
                mockGameSummary(user, game2, difficulty = Difficulty.HARD, duration = 2000, victory = false)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameSummaryService.getSummary(null, null, pageable)

        assertEquals(2, result.content.size)
        assertEquals(1000, result.content[0].duration)
        assertEquals(2000, result.content[1].duration)
    }

    @Test
    fun `get summary with filtering by difficulty`() {
        val user = userRepository.save(mockUser())

        val gameEasy = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val gameHard = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameSummaryRepository.saveAll(
            listOf(
                mockGameSummary(user, gameEasy, difficulty = Difficulty.EASY, duration = 1000),
                mockGameSummary(user, gameHard, difficulty = Difficulty.HARD, duration = 2000)
            )
        )

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameSummaryService.getSummary(Difficulty.EASY, null, pageable)

        assertEquals(1, result.content.size)
        assertEquals(Difficulty.EASY, result.content[0].difficulty)
    }

    @Test
    fun `get summary with filtering by victory`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        gameSummaryRepository.saveAll(listOf(
            mockGameSummary(user, game1, victory = true),
            mockGameSummary(user, game2, victory = false)
        ))

        val pageable = PageRequest.of(0, 10, Sort.by("duration").ascending())

        val result = gameSummaryService.getSummary(null, true, pageable)

        assertEquals(1, result.content.size)
        assertTrue(result.content.all { it.victory })
    }

    @Test
    fun `get summary with sorting by total moves descending`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        val summary1 = mockGameSummary(user, game1, totalMoves = 10)
        val summary2 = mockGameSummary(user, game2, totalMoves = 20)

        gameSummaryRepository.saveAll(listOf(summary1, summary2))

        val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "totalMoves"))

        val result = gameSummaryService.getSummary(null, null, pageable)

        assertEquals(2, result.content.size)
        assertTrue(result.content[0].totalMoves > result.content[1].totalMoves)
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

        assertEquals(game.createdAt.truncate(), gameSummary.createdAt.truncate())
        assertEquals(game.finishedAt!!.truncate(), gameSummary.finishedAt.truncate())
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

    private fun LocalDateTime.truncate(): LocalDateTime = truncatedTo(ChronoUnit.SECONDS)
}
