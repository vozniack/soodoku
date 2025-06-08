package dev.vozniack.soodoku.core.domain.entity

import dev.vozniack.soodoku.core.domain.types.MoveType
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
@Table(name = "moves")
data class Move(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false) val game: Game,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var type: MoveType = MoveType.NORMAL,

    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = true) var revertedAt: LocalDateTime? = null,

    @Column(name = "row_index", nullable = false) val row: Int,
    @Column(name ="col_index", nullable = false) val col: Int,

    @Column(nullable = false) val before: Int,
    @Column(nullable = false) val after: Int
)
