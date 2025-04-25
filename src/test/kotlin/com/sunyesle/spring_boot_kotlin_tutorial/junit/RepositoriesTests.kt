package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.article.Article
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleRepository
import com.sunyesle.spring_boot_kotlin_tutorial.user.Role
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository
) {
    @Test
    fun `When finByIdOrNull then return Article`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        entityManager.persist(johnDoe)
        val article = Article("Lorem", "Lorem", "dolor sit amet", johnDoe)
        entityManager.persist(article)
        entityManager.flush()
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        entityManager.persist(johnDoe)
        entityManager.flush()
        val user = userRepository.findByUsername(johnDoe.username)
        assertThat(user).isEqualTo(johnDoe)
    }
}