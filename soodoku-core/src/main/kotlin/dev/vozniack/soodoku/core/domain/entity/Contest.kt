package dev.vozniack.soodoku.core.domain.entity

import dev.vozniack.soodoku.core.domain.types.ContestStatus
import dev.vozniack.soodoku.core.domain.types.Difficulty
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
@Table(name = "contests")
data class Contest(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true) var createdBy: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var status: ContestStatus = ContestStatus.INITIATED,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var difficulty: Difficulty,

    @Column(nullable = true, length = 128) var initialBoard: String,
    @Column(nullable = true, length = 128) var solvedBoard: String,

    @Column(nullable = false) val startedAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = true) var updatedAt: LocalDateTime? = null,
    @Column(nullable = true) var finishedAt: LocalDateTime? = null,
)
