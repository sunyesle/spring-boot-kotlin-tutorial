package com.sunyesle.spring_boot_kotlin_tutorial.article

import com.sunyesle.spring_boot_kotlin_tutorial.user.UserResponse
import java.time.LocalDateTime

data class ArticleResponse(
    var id: Long?,
    var title: String,
    var headline: String,
    var content: String,
    var author: UserResponse,
    var slug: String,
    var addedAt: LocalDateTime
) {
    companion object {
        fun of(article: Article): ArticleResponse {
            return ArticleResponse(
                id = article.id,
                title = article.title,
                headline = article.headline,
                content = article.content,
                author = UserResponse.of(article.author),
                slug = article.slug,
                addedAt = article.addedAt
            )
        }
    }
}
