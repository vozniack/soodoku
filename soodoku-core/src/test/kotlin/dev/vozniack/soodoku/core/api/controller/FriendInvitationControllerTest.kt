package dev.vozniack.soodoku.core.api.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.vozniack.soodoku.core.AbstractWebMvcTest
import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.repository.FriendRepository
import dev.vozniack.soodoku.core.domain.repository.UserRepository
import dev.vozniack.soodoku.core.fixture.mockFriendInvitationRequestDto
import dev.vozniack.soodoku.core.fixture.mockUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class FriendInvitationControllerTest @Autowired constructor(
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
    fun `get sent invitations`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val thirdUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = secondUser))
        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = thirdUser))
        friendInvitationRepository.save(FriendInvitation(sender = secondUser, receiver = thirdUser))

        authenticate(user.email)

        val response: List<FriendInvitationDto> = objectMapper.readValue(
            mockMvc.perform(
                get("/api/friends/invitations/sent").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(2, response.size)
    }

    @Test
    fun `get received invitations`() {
        val user = userRepository.save(mockUser())
        val secondUser = userRepository.save(mockUser("jane.doe@soodoku.com"))
        val thirdUser = userRepository.save(mockUser("jenny.doe@soodoku.com"))

        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = secondUser))
        friendInvitationRepository.save(FriendInvitation(sender = user, receiver = thirdUser))
        friendInvitationRepository.save(FriendInvitation(sender = secondUser, receiver = thirdUser))

        authenticate(thirdUser.email)

        val response: List<FriendInvitationDto> = objectMapper.readValue(
            mockMvc.perform(
                get("/api/friends/invitations/received").contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(2, response.size)
    }

    @Test
    fun `invite user`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser(email = "jane.doe@soodoku.com", username = "janedoe"))

        authenticate(sender.email)

        val response: FriendInvitationDto = objectMapper.readValue(
            mockMvc.perform(
                post("/api/friends/invitations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockFriendInvitationRequestDto()))
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(sender.username, response.sender.username)
        assertEquals(receiver.username, response.receiver.username)
    }

    @Test
    fun `accept invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        authenticate(receiver.email)

        val response: FriendInvitationDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/friends/invitations/${invitation.id}/accept")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(response.id, invitation.id)
    }

    @Test
    fun `reject invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        authenticate(receiver.email)

        val response: FriendInvitationDto = objectMapper.readValue(
            mockMvc.perform(
                put("/api/friends/invitations/${invitation.id}/reject")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk).andReturn().response.contentAsString
        )

        assertEquals(response.id, invitation.id)
    }

    @Test
    fun `delete invitation`() {
        val sender = userRepository.save(mockUser())
        val receiver = userRepository.save(mockUser("jane.doe@soodoku.com"))

        val invitation: FriendInvitation = friendInvitationRepository.save(
            FriendInvitation(sender = sender, receiver = receiver)
        )

        authenticate(sender.email)

        mockMvc.perform(
            delete("/api/friends/invitations/${invitation.id}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString
    }
}
