package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.common.format
import com.sunyesle.spring_boot_kotlin_tutorial.common.toSlug
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExtensionsTests {

    @Test
    fun testFormat(){
        println(LocalDateTime.now().format())
    }

    @Test
    fun testToSlug() {
        println("test text".toSlug())
    }
}
