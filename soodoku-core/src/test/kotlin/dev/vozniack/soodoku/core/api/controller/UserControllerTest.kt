package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.GameDto
import dev.vozniack.soodoku.core.api.dto.UserDto
import dev.vozniack.soodoku.core.domain.extension.toGame
import dev.vozniack.soodoku.core.domain.repository.GameRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.domain.types.Difficulty
import dev.vozniack.soodoku.core.mock.mockUser
import dev.vozniack.soodoku.core.mock.mockUserLanguageUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserPasswordUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserThemeUpdateDto
import dev.vozniack.soodoku.core.mock.mockUserUsernameUpdateDto
import dev.vozniack.soodoku.lib.Soodoku
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class UserControllerTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun `clear up before`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun `clear up after`() {
        gameRepository.deleteAll()
        userRepository.deleteAll()

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `get currently logged user with logged user`() {
        val user = userRepository.save(mockUser())

        authenticate(user.email)

        val response: UserDto = objectMapper.readValue(
            mockMvc.perform(
                get("/api/users").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(user.id, response.id)
        assertEquals(user.email, response.email)
    }

    @Test
    fun `get currently logged user with not logged user`() {
        mockMvc.perform(
            get("/api/users").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update username with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserUsernameUpdateDto("newUsername")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update username with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserUsernameUpdateDto("newUsername")

        mockMvc.perform(
            put("/api/users/${user.id}/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update password with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserPasswordUpdateDto("NewP@ssw0rd")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update password with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val dto = mockUserPasswordUpdateDto("NewP@ssw0rd")

        mockMvc.perform(
            put("/api/users/${user.id}/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update language with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserLanguageUpdateDto("pl_PL")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update language with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserLanguageUpdateDto("pl_PL")

        mockMvc.perform(
            put("/api/users/${user.id}/language")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `update theme with logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserThemeUpdateDto("dark")

        authenticate(user.email)

        mockMvc.perform(
            put("/api/users/${user.id}/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
    }

    @Test
    fun `update theme with not logged user`() {
        val user = userRepository.save(mockUser(username = "johndoe", email = "john.doe@soodoku.com"))
        val request = mockUserThemeUpdateDto("dark")

        mockMvc.perform(
            put("/api/users/${user.id}/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get last game with logged user`() {
        val user = userRepository.save(mockUser())
        val game = gameRepository.save(Soodoku(Soodoku.Difficulty.EASY).toGame(user, Difficulty.EASY, 3))

        authenticate(user.email)

        val response: GameDto = objectMapper.readValue(
            mockMvc.perform(
                get("/api/users/lastGame").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(game.id, response.id)
    }

    @Test
    fun `get last game with not logged user`() {
        mockMvc.perform(
            get("/api/users/lastGame").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get finished games with logged user`() {
        val user = userRepository.save(mockUser())

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

        authenticate(user.email)

        val response = mockMvc.perform(
            get("/api/users/games?finished=true").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString

        val content: List<GameDto> = objectMapper.readValue(
            objectMapper.readTree(response)["content"].toString(),
            object : TypeReference<List<GameDto>>() {}
        )

        assertEquals(2, content.size)
    }

    @Test
    fun `get not finished games with logged user`() {
        val user = userRepository.save(mockUser())

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

        authenticate(user.email)

        val response = mockMvc.perform(
            get("/api/users/games?finished=false").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString

        val content: List<GameDto> = objectMapper.readValue(
            objectMapper.readTree(response)["content"].toString(),
            object : TypeReference<List<GameDto>>() {}
        )

        assertEquals(1, content.size)
    }

    @Test
    fun `get games with not logged user`() {
        mockMvc.perform(
            get("/api/users/games?finished=false").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }
}
