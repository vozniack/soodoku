package dev.vozniack.soodoku.core.domain.repository

import dev.vozniack.soodoku.core.domain.entity.Friend
import dev.vozniack.soodoku.core.domain.entity.User
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface FriendRepository : CrudRepository<Friend, UUID> {

    fun findAllByUser(user: User): List<Friend>

    @Query(
        """
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        FROM Friend f
        WHERE (f.user = :user AND f.friend = :friend)
           OR (f.user = :friend AND f.friend = :user)
    """
    )
    fun friendshipExists(user: User, friend: User): Boolean

    fun deleteByUserAndFriend(user: User, friend: User)
}
