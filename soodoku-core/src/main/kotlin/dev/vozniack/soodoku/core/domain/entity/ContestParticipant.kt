package dev.vozniack.soodoku.core.domain.entity

import dev.vozniack.soodoku.core.domain.types.ContestParticipantStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "contest_participants")
data class ContestParticipant(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var status: ContestParticipantStatus = ContestParticipantStatus.INVITED,

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false) var contest: Contest? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = true) var game: Game? = null,

    @Column(nullable = true) var place: Int? = null,

    @Column(nullable = true) var finishedAt: LocalDateTime? = null,
)
