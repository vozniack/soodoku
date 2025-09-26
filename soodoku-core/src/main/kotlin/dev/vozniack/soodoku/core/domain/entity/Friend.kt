package dev.vozniack.soodoku.core.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "friends", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "friend_id"])])
data class Friend(

    @Id
    @Column(nullable = false)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    val friend: User,

    @Column(nullable = false)
    val since: LocalDateTime = LocalDateTime.now()
)
