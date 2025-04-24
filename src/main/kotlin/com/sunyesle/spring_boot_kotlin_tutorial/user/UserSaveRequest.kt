package com.sunyesle.spring_boot_kotlin_tutorial.user

import jakarta.validation.constraints.NotBlank

data class UserSaveRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String,
    @field:NotBlank val firstname: String,
    @field:NotBlank val lastname: String,
    val description: String? = null
)
