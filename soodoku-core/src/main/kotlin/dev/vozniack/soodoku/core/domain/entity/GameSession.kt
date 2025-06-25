package dev.vozniack.soodoku.core.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "game_sessions")
data class GameSession(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false) val game: Game,

    @Column(nullable = false) val startedAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = true) var pausedAt: LocalDateTime? = null
)
