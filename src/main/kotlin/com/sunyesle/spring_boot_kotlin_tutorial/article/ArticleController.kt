package com.sunyesle.spring_boot_kotlin_tutorial.article
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/article")
class ArticleController(private val service: ArticleService) {

    @GetMapping
    fun findAll() = service.findAll()

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) = service.findBySlug(slug) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article dose not exist")
}