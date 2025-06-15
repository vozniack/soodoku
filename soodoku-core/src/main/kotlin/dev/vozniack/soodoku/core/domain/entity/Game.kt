package dev.vozniack.soodoku.core.domain.entity

import dev.vozniack.soodoku.core.domain.types.Difficulty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "games")
data class Game(

    @Id
    @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @Column(nullable = false) var initialBoard: String,
    @Column(nullable = false) var solvedBoard: String,
    @Column(nullable = false) var currentBoard: String,
    @Column(nullable = false) var locks: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var difficulty: Difficulty,

    @Column(nullable = false) var hints: Int,

    @Column(nullable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = true) var updatedAt: LocalDateTime? = null,
    @Column(nullable = true) var finishedAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) var user: User? = null,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var moves: MutableList<Move> = mutableListOf()
)
