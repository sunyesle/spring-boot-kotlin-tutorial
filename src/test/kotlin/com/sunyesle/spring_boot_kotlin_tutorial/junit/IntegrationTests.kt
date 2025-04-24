package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.sunyesle.spring_boot_kotlin_tutorial.article.Article
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleRepository
import com.sunyesle.spring_boot_kotlin_tutorial.common.DatabaseCleanup
import com.sunyesle.spring_boot_kotlin_tutorial.common.toSlug
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import kotlin.test.Test

/**
 *  `junit.jupiter.testinstance.lifecycle.default = per_class`
 *  테스트 인스턴스의 생명주기를 클래스 단위로 가져가게 설정하면
 *  `@BeforeAll`, `@AfterAll`을 static이 아닌 메서드에 사용할 수 있다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val userRepository: UserRepository,
    @Autowired val articleRepository: ArticleRepository,
    @Autowired val databaseCleanup: DatabaseCleanup
) {

    @BeforeAll
    fun setUp() {
        println(">> Setup")
        databaseCleanup.execute()

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

    @Test
    fun `Assert blog page title, content and status code`() {
        val entity = restTemplate.getForEntity<String>("/")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("<h1>Blog</h1>", "Lorem")
    }

    @Test
    fun `Assert article page title, content and status code`() {
        println(">> Assert article page title, content and status code")
        val title = "Lorem"
        val entity = restTemplate.getForEntity<String>("/article/${title.toSlug()}")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(title, "Lorem", "dolor sit amet")
    }

    @AfterAll
    fun tearDown() {
        println(">> Tear down")
    }
}
