package com.sunyesle.spring_boot_kotlin_tutorial.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

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

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(code = "ERROR", message = "에러가 발생했습니다.")
        )
    }
}
