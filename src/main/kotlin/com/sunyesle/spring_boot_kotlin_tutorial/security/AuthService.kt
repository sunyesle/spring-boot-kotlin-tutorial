package com.sunyesle.spring_boot_kotlin_tutorial.security

import com.sunyesle.spring_boot_kotlin_tutorial.user.User
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

        val accessToken = createAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)

        refreshTokenRepository.save(RefreshToken(refreshToken))

        return TokenResponse(accessToken, refreshToken)
    }

    fun reissueAccessToken(request: RefreshTokenRequest): AccessTokenResponse {
        val username = try {
            jwtUtil.extractClaimsFromRefreshToken(request.refreshToken).subject
        } catch (e: Exception) {
            throw InvalidRefreshTokenException("JWT validation failed", e)
        }

        val user = userRepository.findByUsername(username)
            ?: throw InvalidRefreshTokenException("User not found")

        if (!refreshTokenRepository.existsById(request.refreshToken)) {
            throw InvalidRefreshTokenException("Refresh token not found")
        }

        val accessToken = createAccessToken(user)
        return AccessTokenResponse(accessToken)
    }

    private fun createAccessToken(user: User): String {
        return jwtUtil.generateAccessToken(user.username, listOf("ROLE_" + user.role.name))
    }
}
