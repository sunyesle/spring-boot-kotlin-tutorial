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
    @Value("\${jwt.secret}") secret: String
) {
    companion object {
        const val CLAIM_KEY_ROLES = "roles"
    }

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    private val parser: JwtParser = Jwts.parser().verifyWith(secretKey).build()

    fun generateToken(roles: List<String>): String {
        val now = Instant.now()
        val expiryDate = now.plus(Duration.ofMillis(3600000))

        return Jwts.builder()
            .claim(CLAIM_KEY_ROLES, roles)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiryDate))
            .signWith(secretKey)
            .compact()
    }

    fun extractClaims(token: String): Claims {
        return parser.parseSignedClaims(token).payload
    }
}
