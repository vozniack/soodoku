package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface FriendInvitationRepository : CrudRepository<FriendInvitation, UUID> {

    fun findAllBySenderAndStatus(sender: User, status: InvitationStatus): List<FriendInvitation>

    fun findAllByReceiverAndStatus(receiver: User, status: InvitationStatus): List<FriendInvitation>

    fun findBySenderAndReceiverAndStatus(sender: User, receiver: User, status: InvitationStatus): FriendInvitation?

    @Query("""SELECT f  FROM FriendInvitation f WHERE (f.sender = :user OR f.receiver = :user) AND f.status = :status""")
    fun findAllPendingInvites(
        @Param("user") user: User,
        @Param("status") status: InvitationStatus = InvitationStatus.PENDING
    ): List<FriendInvitation>
}
