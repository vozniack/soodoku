package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface FriendInvitationRepository : CrudRepository<FriendInvitation, UUID> {

    fun findAllBySender(sender: User): List<FriendInvitation>

    fun findAllByReceiver(receiver: User): List<FriendInvitation>

    fun findBySenderAndReceiver(sender: User, receiver: User): FriendInvitation?
}
