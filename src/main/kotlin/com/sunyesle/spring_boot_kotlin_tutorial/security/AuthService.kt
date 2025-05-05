package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.sunyesle.spring_boot_kotlin_tutorial.user.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun generateToken(request: TokenRequest): TokenResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw BadCredentialsException("Bad credentials")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Bad credentials")
        }

        val accessToken = jwtUtil.generateAccessToken(user.username, listOf("ROLE_" + user.role.name))
        val refreshToken = jwtUtil.generateRefreshToken(user.username)

        refreshTokenRepository.save(RefreshToken(refreshToken))

        return TokenResponse(accessToken, refreshToken)
    }
}
