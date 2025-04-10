package com.sunyesle.spring_boot_kotlin_tutorial

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/article")
class ArticleController(private val repository: ArticleRepository) {

    @GetMapping
    fun findAll() = repository.findAllByOrderByAddedAtDesc()

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) =
        repository.findBySlug(slug) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article dose not exist")
}

@RestController
@RequestMapping("/api/user")
class UserController(private val service: UserService) {

    @GetMapping
    fun findAll() = service.findAll()

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) =
        service.findByLogin(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist")

    @PostMapping
    fun save(@RequestBody request: UserSaveRequest): ResponseEntity<User> {
        val savedUser = service.save(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }
}
