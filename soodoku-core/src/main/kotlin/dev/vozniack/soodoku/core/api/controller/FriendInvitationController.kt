package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.api.dto.FriendInvitationRequestDto
import dev.vozniack.soodoku.core.service.FriendInvitationService
import java.util.UUID
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends/invitations")
class FriendInvitationController(private val friendInvitationService: FriendInvitationService) {

    @GetMapping("/sent")
    fun getSent(): List<FriendInvitationDto> =
        friendInvitationService.getSent()

    @GetMapping("/received")
    fun getReceived(): List<FriendInvitationDto> =
        friendInvitationService.getReceived()

    @PostMapping
    fun invite(@RequestBody request: FriendInvitationRequestDto): FriendInvitationDto =
        friendInvitationService.invite(request)

    @PutMapping("/{id}/accept")
    fun accept(@PathVariable id: UUID): FriendInvitationDto =
        friendInvitationService.accept(id)

    @PutMapping("/{id}/reject")
    fun reject(@PathVariable id: UUID): FriendInvitationDto =
        friendInvitationService.reject(id)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) {
        friendInvitationService.delete(id)
    }
}
