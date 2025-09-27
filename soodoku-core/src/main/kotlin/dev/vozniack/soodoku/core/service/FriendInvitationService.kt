package dev.vozniack.soodoku.core.service

import dev.vozniack.soodoku.core.api.dto.FriendInvitationDto
import dev.vozniack.soodoku.core.api.dto.FriendInvitationRequestDto
import dev.vozniack.soodoku.core.api.mapper.toDto
import dev.vozniack.soodoku.core.domain.entity.FriendInvitation
import dev.vozniack.soodoku.core.domain.entity.User
import dev.vozniack.soodoku.core.domain.repository.FriendInvitationRepository
import dev.vozniack.soodoku.core.domain.types.InvitationStatus
import dev.vozniack.soodoku.core.internal.exception.ConflictException
import dev.vozniack.soodoku.core.internal.exception.NotFoundException
import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import dev.vozniack.soodoku.core.service.extension.buildFriendInvitationAcceptedEvent
import dev.vozniack.soodoku.core.service.extension.buildFriendInvitationReceivedEvent
import dev.vozniack.soodoku.core.service.extension.buildFriendInvitationRejectedEvent
import dev.vozniack.soodoku.core.service.extension.buildFriendInvitationRemovedEvent
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FriendInvitationService(
    private val friendInvitationRepository: FriendInvitationRepository,
    private val userService: UserService,
    private val friendService: FriendService,
    private val notificationService: NotificationService
) {

    fun getSent(): List<FriendInvitationDto> = friendInvitationRepository.findAllBySenderAndStatus(
        getCurrentlyLoggedUser(), InvitationStatus.PENDING
    ).map { it.toDto() }

    fun getReceived(): List<FriendInvitationDto> = friendInvitationRepository.findAllByReceiverAndStatus(
        getCurrentlyLoggedUser(), InvitationStatus.PENDING
    ).map { it.toDto() }

    fun invite(request: FriendInvitationRequestDto): FriendInvitationDto {
        val sender: User = getCurrentlyLoggedUser()

        val receiver: User = userService.findByUsername(request.receiverUsername)
            ?: throw NotFoundException("Not found user ${request.receiverUsername}")

        friendInvitationRepository.findBySenderAndReceiverAndStatus(sender, receiver, InvitationStatus.PENDING)
            ?.let { throw ConflictException("User ${sender.username} already invited user ${receiver.username}") }

        val invitation = friendInvitationRepository.save(FriendInvitation(sender = sender, receiver = receiver))

        notificationService.sendSseEvent(invitation.receiver.id, buildFriendInvitationReceivedEvent(invitation))

        return invitation.toDto()
    }

    fun accept(id: UUID): FriendInvitationDto {
        val user: User = getCurrentlyLoggedUser()

        var invitation: FriendInvitation = friendInvitationRepository.findById(id).orElseThrow {
            NotFoundException("Not found invitation with id $id")
        }?.takeIf { it.receiver.id == user.id } ?: throw ConflictException("You don't have access to this resource")

        friendInvitationRepository.findBySenderAndReceiverAndStatus(
            invitation.receiver, invitation.sender, InvitationStatus.PENDING
        )?.let { friendInvitationRepository.delete(it) }

        friendService.create(invitation)

        invitation = friendInvitationRepository.save(invitation.apply { status = InvitationStatus.ACCEPTED })

        notificationService.sendSseEvent(invitation.sender.id, buildFriendInvitationAcceptedEvent(invitation))

        return invitation.toDto()
    }

    fun reject(id: UUID): FriendInvitationDto {
        val user: User = getCurrentlyLoggedUser()

        var invitation: FriendInvitation = friendInvitationRepository.findById(id).orElseThrow {
            NotFoundException("Not found invitation with id $id")
        }?.takeIf { it.receiver.id == user.id } ?: throw ConflictException("You don't have access to this resource")

        invitation = friendInvitationRepository.save(invitation.apply { status = InvitationStatus.REJECTED })

        friendInvitationRepository.findBySenderAndReceiverAndStatus(
            invitation.receiver, invitation.sender, InvitationStatus.PENDING
        )?.let { friendInvitationRepository.delete(it) }

        notificationService.sendSseEvent(invitation.sender.id, buildFriendInvitationRejectedEvent(invitation))

        return invitation.toDto()
    }

    fun delete(id: UUID) {
        val user: User = getCurrentlyLoggedUser()

        val invitation: FriendInvitation = friendInvitationRepository.findById(id).orElseThrow {
            NotFoundException("Not found invitation with id $id")
        }?.takeIf { it.sender.id == user.id && it.status == InvitationStatus.PENDING }
            ?: throw ConflictException("You don't have access to this resource")

        friendInvitationRepository.delete(invitation)

        notificationService.sendSseEvent(invitation.receiver.id, buildFriendInvitationRemovedEvent(invitation))
    }

    private fun getCurrentlyLoggedUser(): User = userService.currentlyLoggedUser()
        ?: throw UnauthorizedException("You don't have access to this resource")
}
