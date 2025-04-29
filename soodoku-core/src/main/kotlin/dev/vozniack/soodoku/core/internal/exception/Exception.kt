package dev.vozniack.soodoku.core.internal.exception

import java.time.Instant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class BadRequestException(msg: String) : Exception(msg)

class UnauthorizedException(msg: String) : Exception(msg)

class ForbiddenException(msg: String) : Exception(msg)

class NotFoundException(msg: String) : Exception(msg)

class ConflictException(msg: String) : Exception(msg)

data class ExceptionResponse(val message: String?, val status: Int, val timestamp: Long)

fun HttpStatus.toResponseEntity(message: String?): ResponseEntity<ExceptionResponse> = ResponseEntity(
    ExceptionResponse(message = message, status = value(), timestamp = Instant.now().epochSecond), this
)
