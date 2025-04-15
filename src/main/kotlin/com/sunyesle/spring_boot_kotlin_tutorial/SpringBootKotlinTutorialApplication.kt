package com.sunyesle.spring_boot_kotlin_tutorial

import com.sunyesle.spring_boot_kotlin_tutorial.common.BlogProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BlogProperties::class)
class SpringBootKotlinTutorialApplication

fun main(args: Array<String>) {
	runApplication<SpringBootKotlinTutorialApplication>(*args)
}
