package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.FriendDto
import dev.vozniack.soodoku.core.api.dto.UserSimpleDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.fixture.mockFriend
import dev.vozniack.soodoku.core.fixture.mockUser
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class FriendControllerTest @Autowired constructor(
    private val friendInvitationRepository: FriendInvitationRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
    context: WebApplicationContext
) : AbstractWebMvcTest(context) {

    private val objectMapper = jacksonObjectMapper()

    @AfterEach
    fun `clean up`() {
        friendInvitationRepository.deleteAll()
        friendRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `get friends`() {
        val user = userRepository.save(mockUser())
        val friendUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        friendRepository.save(mockFriend(user = user, friend = friendUser))
        friendRepository.save(mockFriend(user = friendUser, friend = user))

        authenticate(user.email)

        val response: List<FriendDto> = objectMapper.readValue(
            mockMvc.perform(
                get("/api/friends").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(1, response.size)
        assertEquals(friendUser.username, response.first().friend.username)
    }

    @Test
    fun `get friend candidates`() {
        val user = userRepository.save(mockUser())
        val friendUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val invitedUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))
        val candidateUser = userRepository.save(mockUser("jan.doe@soodoku.com", username = "jandoe"))

        friendRepository.save(mockFriend(user = user, friend = friendUser))
        friendRepository.save(mockFriend(user = friendUser, friend = user))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = invitedUser))

        authenticate(user.email)

        val response: List<UserSimpleDto> = objectMapper.readValue(
            mockMvc.perform(
                get("/api/friends/candidates")
                    .param("search", "jan")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(1, response.size)
        assertEquals(candidateUser.username, response.first().username)
    }

    @Test
    fun `remove friend`() {
        val user = userRepository.save(mockUser())
        val friendUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val friend = friendRepository.save(mockFriend(user = user, friend = friendUser))
        friendRepository.save(mockFriend(user = friendUser, friend = user))

        authenticate(user.email)

        mockMvc.perform(
            delete("/api/friends/${friend.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        assertEquals(0, friendRepository.count())
    }

    @Test
    fun `remove friend not belonging to user`() {
        val user = userRepository.save(mockUser())
        val friendUser = userRepository.save(mockUser("jane.doe@soodoku.com"))

        friendRepository.save(mockFriend(user = user, friend = friendUser))
        val friend = friendRepository.save(mockFriend(user = friendUser, friend = user))

        authenticate(user.email)

        mockMvc.perform(
            delete("/api/friends/${friend.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict)
    }

    @Test
    fun `get friends with anonymous user`() {
        mockMvc.perform(
            get("/api/friends").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `get friend candidates with anonymous user`() {
        mockMvc.perform(
            get("/api/friends/candidates").param("search", "doe").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `remove friend with anonymous user`() {
        mockMvc.perform(
            delete("/api/friends/${UUID.randomUUID()}").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }
}
