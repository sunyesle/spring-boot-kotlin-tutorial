package com.sunyesle.spring_boot_kotlin_tutorial.security

class InvalidRefreshTokenException : RuntimeException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
}
