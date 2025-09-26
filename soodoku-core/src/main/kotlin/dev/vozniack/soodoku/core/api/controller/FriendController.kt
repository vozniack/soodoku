package dev.vozniack.soodoku.core.api.controller

import dev.vozniack.soodoku.core.api.dto.FriendDto
import dev.vozniack.soodoku.core.api.dto.UserSimpleDto
import dev.vozniack.soodoku.core.service.FriendService
import java.util.UUID
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends")
class FriendController(private val friendService: FriendService) {

    @GetMapping
    fun getFriends(): List<FriendDto> = friendService.getFriends()

    @GetMapping("/candidates")
    fun getFriendCandidates(@RequestParam search: String): List<UserSimpleDto> =
        friendService.getFriendCandidates(search)

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: UUID) = friendService.remove(id)
}
