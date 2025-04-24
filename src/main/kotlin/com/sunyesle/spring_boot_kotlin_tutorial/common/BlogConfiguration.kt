package com.sunyesle.spring_boot_kotlin_tutorial.common

import com.sunyesle.spring_boot_kotlin_tutorial.article.Article
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleRepository
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogConfiguration {

    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        articleRepository: ArticleRepository
    ) = ApplicationRunner {

        val johnDoe = userRepository.save(User("johnDoe", "password", "John", "Doe"))
        articleRepository.save(
            Article(
                title = "Lorem",
                headline = "Lorem",
                content = "dolor sit amet",
                author = johnDoe
            )
        )
        articleRepository.save(
            Article(
                title = "Ipsum",
                headline = "Ipsum",
                content = "dolor sit amet",
                author = johnDoe
            )
        )
    }
}
