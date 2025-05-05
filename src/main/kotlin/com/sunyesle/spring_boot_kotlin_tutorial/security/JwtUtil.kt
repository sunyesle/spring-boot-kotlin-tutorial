package com.sunyesle.spring_boot_kotlin_tutorial.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.refresh-secret}") refreshSecret: String
) {
    companion object {
        const val CLAIM_KEY_ROLES = "roles"
    }

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    private val refreshSecretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret))
    private val accessTokenParser: JwtParser = Jwts.parser().verifyWith(secretKey).build()
    private val refreshTokenParser: JwtParser = Jwts.parser().verifyWith(refreshSecretKey).build()

    fun generateAccessToken(username: String, roles: List<String>): String {
        val now = Instant.now()
        val expiryDate = now.plus(Duration.ofMillis(3600000))

        return Jwts.builder()
            .subject(username)
            .claim(CLAIM_KEY_ROLES, roles)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiryDate))
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(username: String): String {
        val now = Instant.now()
        val expiryDate = now.plus(Duration.ofMillis(36000000))

        return Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiryDate))
            .signWith(refreshSecretKey)
            .compact()
    }

    fun extractClaimsFromAccessToken(token: String): Claims {
        return accessTokenParser.parseSignedClaims(token).payload
    }

    fun extractClaimsFromRefreshToken(token: String): Claims {
        return refreshTokenParser.parseSignedClaims(token).payload
    }
}
