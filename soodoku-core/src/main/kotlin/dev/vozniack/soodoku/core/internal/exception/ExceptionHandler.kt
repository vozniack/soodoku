package dev.vozniack.soodoku.core.internal.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(
        exception: BadRequestException, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.BAD_REQUEST, request) {
        Companion.logger.warn { it.toLogMessage() }
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(
        exception: UnauthorizedException, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.UNAUTHORIZED, request) {
        Companion.logger.warn { it.toLogMessage() }
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(
        exception: ForbiddenException, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.FORBIDDEN, request) {
        Companion.logger.warn { it.toLogMessage() }
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(
        exception: NotFoundException, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.NOT_FOUND, request) {
        Companion.logger.warn { it.toLogMessage() }
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(
        exception: ConflictException, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.CONFLICT, request) {
        Companion.logger.error { it.toLogMessage() }
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(
        exception: Exception, request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse>? = handle(exception, HttpStatus.INTERNAL_SERVER_ERROR, request) {
        Companion.logger.error { it.stackTraceToString() }
    }

    private fun handle(
        exception: Exception, status: HttpStatus, request: HttpServletRequest, log: (Exception) -> Unit
    ): ResponseEntity<ExceptionResponse>? =
        status.takeUnless { request.getHeader("Accept")?.contains("text/event-stream") == true }?.let {
            it.toResponseEntity(exception.message).also { log(exception) }
        }

    private fun Exception.toLogMessage(): String =
        "${this.javaClass.simpleName}: ${this.message} {${this.stackTrace.firstOrNull()}}"

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
