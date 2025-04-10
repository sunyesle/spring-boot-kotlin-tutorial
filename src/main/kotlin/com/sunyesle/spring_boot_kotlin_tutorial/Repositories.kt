package com.sunyesle.spring_boot_kotlin_tutorial

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User?
}
