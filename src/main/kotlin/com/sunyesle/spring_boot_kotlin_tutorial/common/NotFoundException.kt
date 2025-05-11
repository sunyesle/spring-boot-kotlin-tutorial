package com.sunyesle.spring_boot_kotlin_tutorial.common

class NotFoundException : RuntimeException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
}
