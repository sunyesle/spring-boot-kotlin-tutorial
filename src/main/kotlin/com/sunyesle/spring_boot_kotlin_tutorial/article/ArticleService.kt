package com.sunyesle.spring_boot_kotlin_tutorial.article

import org.springframework.stereotype.Service

@Service
class ArticleService(private val repository: ArticleRepository) {
    fun findAll() = repository.findAllByOrderByAddedAtDesc().map(ArticleResponse::of)

    fun findBySlug(slug: String) = repository.findBySlug(slug)?.let(ArticleResponse::of)
}
