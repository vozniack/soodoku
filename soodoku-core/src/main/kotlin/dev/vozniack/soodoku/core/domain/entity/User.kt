package dev.vozniack.soodoku.core.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(

    @Id @Column(nullable = false) val id: UUID = UUID.randomUUID(),

    @Column(nullable = false) var email: String,
    @Column(nullable = true) var password: String? = null,

    @Column(nullable = false) var username: String,
    @Column(nullable = false) var language: String,
    @Column(nullable = false) var theme: String,
)
