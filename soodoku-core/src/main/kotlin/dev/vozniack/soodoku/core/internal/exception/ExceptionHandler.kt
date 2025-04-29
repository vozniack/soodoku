package dev.vozniack.soodoku.core.internal.exception

import dev.vozniack.soodoku.core.internal.logging.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleBadRequestException(exception: BadRequestException): ResponseEntity<ExceptionResponse> =
        HttpStatus.BAD_REQUEST.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.warn { exception.toLogMessage() }
        }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleUnauthorizedException(exception: UnauthorizedException): ResponseEntity<ExceptionResponse> =
        HttpStatus.UNAUTHORIZED.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.warn { exception.toLogMessage() }
        }

    @ExceptionHandler(value = [ForbiddenException::class])
    fun handleForbiddenException(exception: ForbiddenException): ResponseEntity<ExceptionResponse> =
        HttpStatus.FORBIDDEN.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.warn { exception.toLogMessage() }
        }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ExceptionResponse> =
        HttpStatus.NOT_FOUND.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.warn { exception.toLogMessage() }
        }

    @ExceptionHandler(value = [ConflictException::class])
    fun handleConflictException(exception: ConflictException): ResponseEntity<ExceptionResponse> =
        HttpStatus.CONFLICT.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.error { exception.toLogMessage() }
        }

    @ExceptionHandler(value = [Exception::class])
    fun handleGeneralException(exception: Exception): ResponseEntity<ExceptionResponse> =
        HttpStatus.INTERNAL_SERVER_ERROR.toResponseEntity(exception.message).also {
            ResponseExceptionHandler.logger.error { exception.stackTraceToString() }
        }

    private fun Exception.toLogMessage(): String = "${this.javaClass.simpleName}: ${this.message} {${this.stackTrace[0]}}"

    companion object : KLogging()
}
