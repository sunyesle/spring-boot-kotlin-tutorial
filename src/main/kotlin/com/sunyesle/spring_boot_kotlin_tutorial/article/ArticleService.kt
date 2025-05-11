package com.sunyesle.spring_boot_kotlin_tutorial.article

import com.sunyesle.spring_boot_kotlin_tutorial.common.NotFoundException
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    fun findAll() = articleRepository.findAllByOrderByAddedAtDesc().map(ArticleResponse::of)

    fun findBySlug(slug: String) = articleRepository.findBySlug(slug)?.let(ArticleResponse::of)

    fun save(request: ArticleRequest, username: String): ArticleResponse {
        val user = userRepository.findByUsername(username)
            ?: throw NotFoundException("User $username not found")

        val article = Article(
            title = request.title,
            headline = request.headline,
            content = request.content,
            author = user
        )
        return articleRepository.save(article).let(ArticleResponse::of)
    }
}
