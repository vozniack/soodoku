package dev.vozniack.soodoku.core.util

inline fun throwIfTrue(exception: Exception, block: () -> Boolean): Unit? = if (block()) throw exception else null
