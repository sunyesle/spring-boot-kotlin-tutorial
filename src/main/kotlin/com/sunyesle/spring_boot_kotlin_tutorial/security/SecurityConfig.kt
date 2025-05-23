package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

    @Bean
    fun authenticationEntryPoint(objectMapper: ObjectMapper) : AuthenticationEntryPoint =
        CustomAuthenticationEntryPoint(objectMapper)

    @Bean
    fun accessDeniedHandler(objectMapper: ObjectMapper) : AccessDeniedHandler =
        CustomAccessDeniedHandler(objectMapper)

    @Bean
    fun userDetailService(userRepository: UserRepository) : UserDetailsService =
        JwtUserDetailsService(userRepository)

    @Bean
    fun jwtAuthenticationFilter(authenticationManager: AuthenticationManager): JwtAuthenticationFilter =
        JwtAuthenticationFilter(authenticationManager)

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        jwtAuthenticationProvider: JwtAuthenticationProvider
    ): AuthenticationManager {
        val builder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        builder.authenticationProvider(jwtAuthenticationProvider)
        return builder.build()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        authenticationEntryPoint: AuthenticationEntryPoint,
        accessDeniedHandler: AccessDeniedHandler
    ): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { it.frameOptions { frame -> frame.sameOrigin() } }
            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/token").permitAll()
                it.requestMatchers("/api/auth/token/refresh").permitAll()
                it.requestMatchers("/api/auth/test/admin").hasRole("ADMIN")
                it.requestMatchers("/api/auth/test/user").hasRole("USER")
                it.anyRequest().permitAll()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }

        return http.build()
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}
