package com.sunyesle.spring_boot_kotlin_tutorial.security

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class RefreshToken (
    @Id var refreshToken: String
)
