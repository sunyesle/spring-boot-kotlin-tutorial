package com.sunyesle.spring_boot_kotlin_tutorial.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
)
