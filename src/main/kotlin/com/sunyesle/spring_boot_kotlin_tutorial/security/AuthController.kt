package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authService: AuthService
){

    @PostMapping("/token")
    fun generateToken(
        @RequestBody request: TokenRequest
    ): TokenResponse = authService.generateToken(request)

    @GetMapping("/test")
    fun test() {
        println("test")
    }
}
