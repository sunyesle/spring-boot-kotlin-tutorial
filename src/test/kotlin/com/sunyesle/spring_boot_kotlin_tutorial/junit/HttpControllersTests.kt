package com.sunyesle.spring_boot_kotlin_tutorial.junit

import com.ninjasquad.springmockk.MockkBean
import com.sunyesle.spring_boot_kotlin_tutorial.article.Article
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleController
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleResponse
import com.sunyesle.spring_boot_kotlin_tutorial.article.ArticleService
import com.sunyesle.spring_boot_kotlin_tutorial.user.*
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.Test

@WebMvcTest(controllers = [ArticleController::class, UserController::class])
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userService: UserService

    @MockkBean
    lateinit var articleService: ArticleService

    @Test
    @WithMockUser
    fun `List articles`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        val loremArticle = Article("Lorem", "Lorem", "dolor sit amet", johnDoe)
        val ipsumArticle = Article("Ipsum", "Ipsum", "dolor sit amet", johnDoe)
        every { articleService.findAll() } returns listOf(loremArticle, ipsumArticle).map(ArticleResponse::of)

        mockMvc.perform(get("/api/article").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].author.username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[0].slug").value(loremArticle.slug))
            .andExpect(jsonPath("\$.[1].author.username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[1].slug").value(ipsumArticle.slug))
    }

    @Test
    @WithMockUser
    fun `List users`() {
        val johnDoe = User("johnDoe", "password", Role.USER, "John", "Doe")
        val janeDoe = User("janeDoe", "password", Role.USER, "Jane", "Doe")
        every { userService.findAll() } returns listOf(johnDoe, janeDoe).map(UserResponse::of)

        mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].username").value(johnDoe.username))
            .andExpect(jsonPath("\$.[1].username").value(janeDoe.username))
    }
}