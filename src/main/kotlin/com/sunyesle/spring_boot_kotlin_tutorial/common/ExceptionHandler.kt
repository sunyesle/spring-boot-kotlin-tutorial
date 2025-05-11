package com.sunyesle.spring_boot_kotlin_tutorial.common

import com.sunyesle.spring_boot_kotlin_tutorial.security.InvalidRefreshTokenException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(code = "NOT_FOUND", message = "요청한 리소스를 찾을 수 없습니다.")
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse<List<FieldErrorDto>>> {
        val allErrors = e.bindingResult.fieldErrors.map {
            FieldErrorDto(
                field = it.field,
                message = it.defaultMessage,
                value = it.rejectedValue
            )
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                code = "VALIDATION_FAILED",
                message = "유효성 검증에 실패했습니다.",
                allErrors
            )
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<ErrorResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(code = "UNAUTHORIZED", message = "인증에 실패했습니다.")
        )
    }

    @ExceptionHandler(InvalidRefreshTokenException::class)
    fun handleInvalidRefreshTokenException(e: InvalidRefreshTokenException): ResponseEntity<ErrorResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(code = "INVALID_REFRESH_TOKEN", message = "유효하지 않은 리프레시 토큰입니다.")
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse<Nothing>> {
        logger.error("", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(code = "ERROR", message = "에러가 발생했습니다.")
        )
    }
}
