package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.GameHistoryDto
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.GameHistoryRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.fixture.findEmptyCell
import dev.vozniack.soodoku.core.fixture.mockGameHistory
import dev.vozniack.soodoku.core.fixture.mockMoveRequestDto
import dev.vozniack.soodoku.core.fixture.mockNewGameRequestDto
import dev.vozniack.soodoku.core.fixture.mockNoteRequestDto
import dev.vozniack.soodoku.core.fixture.mockUser
import dev.vozniack.soodoku.core.service.GameService
import dev.vozniack.soodoku.lib.Soodoku
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class GameControllerTest @Autowired constructor(
    context: WebApplicationContext,
    private val gameRepository: GameRepository,
    private val gameHistoryRepository: GameHistoryRepository,
    private val userRepository: UserRepository,
    private val gameService: GameService,
) : AbstractWebMvcTest(context) {

    private val objectMapper = jacksonObjectMapper()

    @AfterEach
    fun `clean up`() {
        gameHistoryRepository.deleteAll()
        gameRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `get game with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                get("/api/games/${gameDto.id}")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `get game with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                get("/api/games/${gameDto.id}")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `get game with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            get("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get ongoing games with anonymous user`() {
        mockMvc.perform(
            get("/api/games/ongoing").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get ongoing games with existing user`() {
        val user = userRepository.save(mockUser())

        gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

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

        authenticate(user.email)

        val response = mockMvc.perform(
            get("/api/games/ongoing").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString

        val content: List<GameDto> = objectMapper.readValue(
            objectMapper.readTree(response)["content"].toString(),
            object : TypeReference<List<GameDto>>() {}
        )

        assertEquals(2, content.size)
    }

    @Test
    fun `get history with anonymous user`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, game1, difficulty = Difficulty.EASY, duration = 1000, victory = true),
                mockGameHistory(user, game2, difficulty = Difficulty.HARD, duration = 2000, victory = false)
            )
        )

        authenticate(user.email)

        mockMvc.perform(
            get("/api/games/history").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk) // #todo unauthorized with better endpoint security
    }

    @Test
    fun `get history with existing user`() {
        val user = userRepository.save(mockUser())

        val game1 = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))
        val game2 = gameRepository.save(Soodoku(Soodoku.Difficulty.HARD).toGame(user, Difficulty.HARD, 3))

        gameHistoryRepository.saveAll(
            listOf(
                mockGameHistory(user, game1, difficulty = Difficulty.EASY, duration = 1000, victory = true),
                mockGameHistory(user, game2, difficulty = Difficulty.HARD, duration = 2000, victory = false)
            )
        )

        authenticate(user.email)

        val response = mockMvc.perform(
            get("/api/games/history").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString

        val content: List<GameHistoryDto> = objectMapper.readValue(
            objectMapper.readTree(response)["content"].toString(),
            object : TypeReference<List<GameHistoryDto>>() {}
        )

        assertEquals(2, content.size)
    }

    @Test
    fun `create new game with anonymous user`() {
        val request = mockNewGameRequestDto(Difficulty.EASY)

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.id)
        assertEquals(Difficulty.EASY.name, response.difficulty.name)
    }

    @Test
    fun `create new game with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val request = mockNewGameRequestDto(Difficulty.EASY)

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.id)
    }

    @Test
    fun `make a move with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        val request = mockMoveRequestDto(row, col, 5)

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `make a move with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        val request = mockMoveRequestDto(row, col, 5)

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `make a move with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        val request = mockMoveRequestDto(row, col, 5)

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            put("/api/games/${gameDto.id}/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `revert last move with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        gameService.move(gameDto.id, mockMoveRequestDto(row = row, col = col, value = 5))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/revert")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `revert last move with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        gameService.move(gameDto.id, mockMoveRequestDto(row = row, col = col, value = 5))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/revert")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `revert last move with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))
        val (row, col) = gameDto.findEmptyCell()

        gameService.move(gameDto.id, mockMoveRequestDto(row = row, col = col, value = 5))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            put("/api/games/${gameDto.id}/revert")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `make a note with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/note")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockNoteRequestDto()))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `make a note with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/note")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockNoteRequestDto()))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `make a note with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            put("/api/games/${gameDto.id}/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockNoteRequestDto()))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `delete notes with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                delete("/api/games/${gameDto.id}/note")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `delete notes with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                delete("/api/games/${gameDto.id}/note")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `delete notes with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            delete("/api/games/${gameDto.id}/note")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `use hint with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/hint")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `use hint with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/hint")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `use hint with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            put("/api/games/${gameDto.id}/hint")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `end game with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/end")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `end game with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/end")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)

        Thread.sleep(128) // waiting for coroutines to save game history
    }

    @Test
    fun `end game with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            put("/api/games/${gameDto.id}/end")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `delete game with anonymous user`() {
        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        mockMvc.perform(
            delete("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        assertTrue(gameRepository.findById(gameDto.id).isEmpty)
    }

    @Test
    fun `delete game with existing user`() {
        val user = userRepository.save(mockUser())
        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        mockMvc.perform(
            delete("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        assertTrue(gameRepository.findById(gameDto.id).isEmpty)
    }

    @Test
    fun `delete game with user different than owner`() {
        val user = userRepository.save(mockUser())
        userRepository.save(mockUser("jane.doe@soodoku.com"))

        authenticate(user.email)

        val gameDto = gameService.new(mockNewGameRequestDto(Difficulty.EASY))

        authenticate("jane.doe@soodoku.com")

        mockMvc.perform(
            delete("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }
}
