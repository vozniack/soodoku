package dev.vozniack.soodoku.core.util

import dev.vozniack.soodoku.core.util.RegexConstants.EMAIL_REGEX
import dev.vozniack.soodoku.core.util.RegexConstants.PASSWORD_REGEX

fun String.matchesEmailRegex() = matches(EMAIL_REGEX.toRegex())

fun String.matchesPasswordRegex() = matches(PASSWORD_REGEX.toRegex())

private object RegexConstants {
    const val EMAIL_REGEX: String = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
    const val PASSWORD_REGEX: String = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[\$&+:;=?@#|'<>^*()%!-]).{6,}\$"
}
