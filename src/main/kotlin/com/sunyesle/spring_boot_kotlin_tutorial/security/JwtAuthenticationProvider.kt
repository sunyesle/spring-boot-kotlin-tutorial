package com.sunyesle.spring_boot_kotlin_tutorial.security

import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val jwtAuthToken = authentication as? JwtAuthenticationToken
            ?: throw JwtAuthenticationException("Unsupported authentication type: ${authentication?.javaClass?.name}")

        val accessToken = jwtAuthToken.jwtToken
            ?: throw JwtAuthenticationException("JWT token is missing")

        val claims: Claims = try {
            jwtUtil.extractClaimsFromAccessToken(accessToken)
        } catch (e: Exception) {
            throw JwtAuthenticationException("JWT validation failed", e)
        }

        val username = claims.subject
        val userDetails: UserDetails = try {
            userDetailsService.loadUserByUsername(username)
        }catch (e: UsernameNotFoundException) {
            throw JwtAuthenticationException("User not found", e)
        }

        return JwtAuthenticationToken(
            userDetails,
            "",
            createGrantedAuthorities(claims)
        )
    }

    private fun createGrantedAuthorities(claims: Claims): Collection<GrantedAuthority> {
        val roles = claims[JwtUtil.CLAIM_KEY_ROLES] as? List<*> ?: return emptyList()
        return roles.filterIsInstance<String>().map { SimpleGrantedAuthority(it) }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication != null && JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
