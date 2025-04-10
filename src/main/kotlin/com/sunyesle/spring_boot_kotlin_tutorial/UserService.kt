package com.sunyesle.spring_boot_kotlin_tutorial

import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun findAll(): List<User> = repository.findAll()

    fun findByLogin(login: String): User? = repository.findByLogin(login)
}
