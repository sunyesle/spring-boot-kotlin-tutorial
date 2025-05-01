package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.sunyesle.spring_boot_kotlin_tutorial.common.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        response?.let {
            it.status = HttpStatus.FORBIDDEN.value()
            it.characterEncoding = "UTF-8"
            it.contentType = MediaType.APPLICATION_JSON_VALUE

            val errorResponse = ErrorResponse<Nothing>(
                code = "FORBIDDEN",
                message = "접근 권한이 없습니다."
            )
            it.writer.write(objectMapper.writeValueAsString(errorResponse))
        }
    }
}