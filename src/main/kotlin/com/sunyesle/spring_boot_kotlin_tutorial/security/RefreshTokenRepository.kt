package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
}
