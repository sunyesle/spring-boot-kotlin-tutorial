package com.sunyesle.spring_boot_kotlin_tutorial.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun findAll(): List<UserResponse> = repository.findAll().map(UserResponse::of)

    fun findByUsername(username: String): UserResponse? = repository.findByUsername(username)?.let(UserResponse::of)

    fun save(request: UserSaveRequest): UserResponse {
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = Role.USER,
            firstname = request.firstname,
            lastname = request.lastname,
            description = request.description
        )
        return repository.save(user).let(UserResponse::of)
    }
}
