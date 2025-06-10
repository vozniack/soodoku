package dev.vozniack.soodoku.core.mock

import dev.vozniack.soodoku.core.domain.entity.User

fun mockUser(
    email: String = "john.doe@soodoku.com",
    password: String = "J0hn123!",
    username: String? = "johndoe"
): User = User(
    email = email,
    password = password,
    username = username
)
