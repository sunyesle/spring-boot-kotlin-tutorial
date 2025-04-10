package com.sunyesle.spring_boot_kotlin_tutorial

import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun findAll(): List<User> = repository.findAll()

    fun findByLogin(login: String): User? = repository.findByLogin(login)

    fun save(request: UserSaveRequest): User {
        val user = User(
            login = request.login,
            firstname = request.firstname,
            lastname = request.lastname,
            description = request.description
        )
        return repository.save(user)
    }
}
