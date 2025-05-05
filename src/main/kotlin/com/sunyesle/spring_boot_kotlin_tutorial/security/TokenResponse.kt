package com.sunyesle.spring_boot_kotlin_tutorial.security

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
