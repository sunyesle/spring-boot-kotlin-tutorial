package com.sunyesle.spring_boot_kotlin_tutorial.article

import com.sunyesle.spring_boot_kotlin_tutorial.common.toSlug
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Article(
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: User,
    var slug: String = title.toSlug(),
    var addedAt: LocalDateTime = LocalDateTime.now(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
