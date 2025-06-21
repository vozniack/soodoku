package dev.vozniack.soodoku.core.domain.entity

import dev.vozniack.soodoku.core.domain.types.Difficulty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "game_summaries")
data class GameSummary(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @OneToOne
    @JoinColumn(name = "game_id", nullable = false) val game: Game,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) val user: User,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) val difficulty: Difficulty,

    @Column(nullable = false) val duration: Long,

    @Column(nullable = false) val missingCells: Int,
    @Column(nullable = false) val totalMoves: Int,
    @Column(nullable = false) val usedHints: Int,

    @Column(nullable = false) val victory: Boolean
)
