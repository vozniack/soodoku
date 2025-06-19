package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.NewGameRequestDto
import dev.vozniack.soodoku.core.api.dto.MoveRequestDto
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.core.service.GameService
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class GameControllerTest @Autowired constructor(
    context: WebApplicationContext,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val gameService: GameService,
) : AbstractWebMvcTest(context) {

    @AfterEach
    fun `clean up`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            get("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `create new game with anonymous user`() {
        val request = NewGameRequestDto(Difficulty.EASY)

        val response: GameDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                post("/api/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.id)
        assertEquals(Difficulty.EASY.name, response.difficulty.name)
    }

    @Test
    fun `create new game with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val request = NewGameRequestDto(Difficulty.EASY)

        val response: GameDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                post("/api/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertNotNull(response.id)
    }

    @Test
    fun `make a move with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        val request = MoveRequestDto(row, col, 5)

        val response: GameDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }


    @Test
    fun `make a move with existing user`() {
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

        val request = MoveRequestDto(row, col, 5)

        val response: GameDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(request))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
    }

    @Test
    fun `make a move with user different than owner`() {
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

        val request = MoveRequestDto(row, col, 5)

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            put("/api/games/${gameDto.id}/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `revert last move with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val (row, col) = gameDto.board
            .withIndex()
            .flatMap { (r, rowArr) -> rowArr.withIndex().map { (c, v) -> Triple(r, c, v) } }
            .first { it.third == 0 }
            .let { it.first to it.second }

        gameService.move(gameDto.id, MoveRequestDto(row = row, col = col, value = 5))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            put("/api/games/${gameDto.id}/revert")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `use hint with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            put("/api/games/${gameDto.id}/hint")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `end game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        val response: GameDto = jacksonObjectMapper().readValue(
            mockMvc.perform(
                put("/api/games/${gameDto.id}/end")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(gameDto.id, response.id)
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

        mockMvc.perform(
            put("/api/games/${gameDto.id}/end")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `delete game with anonymous user`() {
        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        mockMvc.perform(
            delete("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        assertTrue(gameRepository.findById(gameDto.id).isEmpty)
    }

    @Test
    fun `delete game with existing user`() {
        val user = userRepository.save(mockUser())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

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

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.email, null, emptyList()
        )

        val gameDto = gameService.new(NewGameRequestDto(Difficulty.EASY))

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "jane.doe@soodoku.com", null, emptyList()
        )

        mockMvc.perform(
            delete("/api/games/${gameDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }
}
