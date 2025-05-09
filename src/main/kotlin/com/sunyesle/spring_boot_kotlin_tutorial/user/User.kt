package com.sunyesle.spring_boot_kotlin_tutorial.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User(
    var username: String,
    var password: String,
    var role: Role,
    var firstname: String,
    var lastname: String,
    var description: String? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)

enum class Role {
    USER, ADMIN
}
