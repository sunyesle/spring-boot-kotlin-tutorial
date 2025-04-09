package com.sunyesle.spring_boot_kotlin_tutorial

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blog")
data class BlogProperties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}
