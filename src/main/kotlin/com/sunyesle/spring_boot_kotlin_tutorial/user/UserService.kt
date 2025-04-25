package com.sunyesle.spring_boot_kotlin_tutorial.user

import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun findAll(): List<User> = repository.findAll()

    fun findByUsername(username: String): User? = repository.findByUsername(username)

    fun save(request: UserSaveRequest): User {
        val user = User(
            username = request.username,
            password = request.password,
            role = Role.USER,
            firstname = request.firstname,
            lastname = request.lastname,
            description = request.description
        )
        return repository.save(user)
    }
}
