package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.sunyesle.spring_boot_kotlin_tutorial.common.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.let{
            it.status = HttpStatus.UNAUTHORIZED.value()
            it.characterEncoding = "UTF-8"
            it.contentType = MediaType.APPLICATION_JSON_VALUE

            val errorResponse = ErrorResponse<Nothing>(
                code = "UNAUTHORIZED",
                message = "인증 정보가 없거나 유효하지 않습니다."
            )
            it.writer.write(objectMapper.writeValueAsString(errorResponse))
        }
    }
}