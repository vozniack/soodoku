package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.domain.entity.User

fun mockUser(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!"
): User = User(
    email = email,
    password = password
)
