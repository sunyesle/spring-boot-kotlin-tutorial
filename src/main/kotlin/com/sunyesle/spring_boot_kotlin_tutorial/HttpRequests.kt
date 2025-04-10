package com.sunyesle.spring_boot_kotlin_tutorial

data class UserSaveRequest(
    val login: String,
    val firstname: String,
    val lastname: String,
    val description: String? = null
)
