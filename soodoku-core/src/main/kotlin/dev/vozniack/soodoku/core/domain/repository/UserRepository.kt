package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.User
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, UUID> {

    fun findByEmail(email: String): User?

    fun findByUsername(username: String): User?

    fun findByUsernameContainingIgnoreCase(username: String): List<User>
}
