package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.ninjasquad.springmockk.MockkBean
import com.sunyesle.spring_boot_kotlin_tutorial.article.Article
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleRepository
import com.sunyesle.spring_boot_kotlin_tutorial.user.Role
import com.sunyesle.spring_boot_kotlin_tutorial.user.User
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserResponse
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserService
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.Test

@WebMvcTest(excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userService: UserService

    @MockkBean
    lateinit var articleRepository: ArticleRepository

    @Test
    fun `List articles`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        val loremArticle = Article("Lorem", "Lorem", "dolor sit amet", johnDoe)
        val ipsumArticle = Article("Ipsum", "Ipsum", "dolor sit amet", johnDoe)
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(loremArticle, ipsumArticle)

        mockMvc.perform(get("/api/article").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].author.username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[0].slug").value(loremArticle.slug))
            .andExpect(jsonPath("\$.[1].author.username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[1].slug").value(ipsumArticle.slug))
    }

    @Test
    fun `List users`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        val janeDoe = User("janeDoe", "password", Role.USER, "Jane", "Doe")
        every { userService.findAll() } returns listOf(UserResponse.of(johnDoe), UserResponse.of(janeDoe))

        mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[1].username").value(janeDoe.username))
    }

}