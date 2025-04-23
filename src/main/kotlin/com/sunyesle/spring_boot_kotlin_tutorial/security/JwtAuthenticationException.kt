package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException : AuthenticationException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
}
