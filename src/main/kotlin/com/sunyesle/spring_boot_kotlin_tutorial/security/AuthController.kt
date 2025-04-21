package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @GetMapping("/test")
    fun test() {
        println("test")
    }
}
