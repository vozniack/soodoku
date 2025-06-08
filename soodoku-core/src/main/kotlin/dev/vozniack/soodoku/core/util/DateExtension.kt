package dev.vozniack.soodoku.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toISOTime(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
