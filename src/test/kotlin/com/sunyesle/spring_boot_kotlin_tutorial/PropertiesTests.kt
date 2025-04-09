package com.sunyesle.spring_boot_kotlin_tutorial

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PropertiesTests(@Autowired val properties: BlogProperties) {

    @Test
    fun testBlogProperties(){
        assertThat(properties.title).isEqualTo("Blog")
        assertThat(properties.banner.title).isEqualTo("Warning")
        assertThat(properties.banner.content).isEqualTo("The blog will be down tomorrow")
    }
}