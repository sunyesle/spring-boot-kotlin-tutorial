package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User ${username} not found")

        return User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}
